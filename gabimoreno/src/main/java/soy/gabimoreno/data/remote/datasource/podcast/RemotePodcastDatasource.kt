package soy.gabimoreno.data.remote.datasource.podcast

import arrow.core.Either
import com.prof.rssparser.Parser
import soy.gabimoreno.data.remote.mapper.toDomain
import soy.gabimoreno.domain.model.podcast.EpisodesWrapper

class RemotePodcastDatasource(
    private val rssParser: Parser,
) : PodcastDatasource {

    override suspend fun getEpisodes(url: String): Either<Throwable, EpisodesWrapper> {
        return Either.catch {
            val channel = rssParser.getChannel(url)
            channel.toDomain()
        }
    }
}
