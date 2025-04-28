package soy.gabimoreno.data.remote.datasource.podcast

import arrow.core.Either
import com.prof.rssparser.Parser
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import soy.gabimoreno.data.remote.mapper.toDomain
import soy.gabimoreno.di.data.PodcastUrl
import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.domain.model.podcast.EpisodesWrapper

class RemotePodcastDatasource(
    private val rssParser: Parser,
) : PodcastDatasource {

    override fun getEpisodesStream(podcastUrl: PodcastUrl): Either<Throwable, Flow<List<Episode>>> {
        return Either.catch {
            flow {
                val channel = rssParser.getChannel(podcastUrl)
                val episodes = channel.toDomain().episodes

                val chunkSize = 15
                val totalChunks = (episodes.size + chunkSize - 1) / chunkSize

                for (chunkIndex in 0 until totalChunks) {
                    val fromIndex = chunkIndex * chunkSize
                    val toIndex = minOf(fromIndex + chunkSize, episodes.size)
                    emit(episodes.subList(0, toIndex))
                    delay(500L)
                }
            }
        }
    }

    override suspend fun getEpisodes(podcastUrl: PodcastUrl): Either<Throwable, EpisodesWrapper> {
        return Either.catch {
            val channel = rssParser.getChannel(podcastUrl)
            channel.toDomain()
        }
    }
}
