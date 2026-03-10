package soy.gabimoreno.data.remote.datasource.podcast

import arrow.core.Either
import com.prof18.rssparser.RssParser
import com.prof18.rssparser.model.RssChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import okhttp3.Request
import soy.gabimoreno.data.remote.mapper.toDomain
import soy.gabimoreno.di.data.PodcastUrl
import soy.gabimoreno.domain.model.podcast.Episode

class RemotePodcastDatasource(
    private val rssParser: RssParser,
    private val okHttpClient: OkHttpClient,
) : PodcastDatasource {
    override fun getEpisodesStream(podcastUrl: PodcastUrl): Either<Throwable, Flow<List<Episode>>> =
        Either.catch {
            flow {
                val feed = parseFeedSafely(podcastUrl)
                val episodes = feed.toDomain().episodes

                val chunkSize = CHUNK_SIZE
                val totalChunks = (episodes.size + chunkSize - 1) / chunkSize

                for (chunkIndex in 0 until totalChunks) {
                    val fromIndex = chunkIndex * chunkSize
                    val toIndex = minOf(fromIndex + chunkSize, episodes.size)
                    emit(episodes.subList(0, toIndex))
                    delay(DELAY_TIME_MILLIS)
                }
            }
        }

    private suspend fun parseFeedSafely(podcastUrl: PodcastUrl): RssChannel =
        okHttpClient
            .newCall(
                Request
                    .Builder()
                    .url(podcastUrl)
                    .build(),
            ).execute()
            .use { response ->
                val xml = requireNotNull(response.body).string().sanitizeFeedXml()
                rssParser.parse(xml)
            }
}

private fun String.sanitizeFeedXml(): String =
    replace("&amp;gt;", "&gt;")
        .replace("&amp;lt;", "&lt;")

private const val CHUNK_SIZE = 15
private const val DELAY_TIME_MILLIS = 500L
