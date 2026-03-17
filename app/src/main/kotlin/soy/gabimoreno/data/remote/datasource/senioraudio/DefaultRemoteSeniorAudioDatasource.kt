package soy.gabimoreno.data.remote.datasource.senioraudio

import arrow.core.Either
import com.prof18.rssparser.RssParser
import com.prof18.rssparser.model.RssChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import soy.gabimoreno.data.remote.mapper.senioraudio.toDomain
import soy.gabimoreno.di.data.SeniorAudiosUrl
import soy.gabimoreno.domain.model.content.SeniorAudio

class DefaultRemoteSeniorAudioDatasource(
    private val rssParser: RssParser,
    private val okHttpClient: OkHttpClient,
) : RemoteSeniorAudioDatasource {
    override fun getSeniorAudiosStream(
        seniorAudiosUrl: SeniorAudiosUrl,
    ): Either<Throwable, Flow<List<SeniorAudio>>> {
        val seniorAudiosHttpUrl =
            seniorAudiosUrl
                .trim()
                .toHttpUrlOrNull()
                ?: return Either.Left(
                    IllegalArgumentException("Invalid podcast url: $seniorAudiosUrl"),
                )
        return Either.catch {
            flow {
                val feed = parseFeedSafely(seniorAudiosHttpUrl)
                val episodes = feed.toDomain().seniorAudios

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
    }

    private suspend fun parseFeedSafely(seniorAudiosHttpUrl: HttpUrl): RssChannel =
        okHttpClient
            .newCall(
                Request
                    .Builder()
                    .url(seniorAudiosHttpUrl)
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
