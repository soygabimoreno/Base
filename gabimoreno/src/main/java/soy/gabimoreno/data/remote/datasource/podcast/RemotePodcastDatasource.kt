package soy.gabimoreno.data.remote.datasource.podcast

import arrow.core.Either
import com.prof.rssparser.Parser
import soy.gabimoreno.data.remote.mapper.toDomain
import soy.gabimoreno.di.data.PodcastUrl
import soy.gabimoreno.domain.model.podcast.EpisodesWrapper

class RemotePodcastDatasource(
    private val rssParser: Parser,
) : PodcastDatasource {

    override suspend fun getEpisodes(podcastUrl: PodcastUrl): Either<Throwable, EpisodesWrapper> {
        return Either.catch {
            val channel = rssParser.getChannel(podcastUrl)
            channel.toDomain()
        }
    }
}
