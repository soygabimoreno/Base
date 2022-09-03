package soy.gabimoreno.data.network.repository

import arrow.core.Either
import com.prof.rssparser.Parser
import soy.gabimoreno.data.network.mapper.toDomain
import soy.gabimoreno.domain.model.podcast.PodcastSearch
import soy.gabimoreno.domain.repository.PodcastRepository

class NetworkPodcastRepository(
    private val rssParser: Parser
) : PodcastRepository {

    override suspend fun getEpisodes(): Either<Throwable, PodcastSearch> {
        return Either.catch {
            val channel = rssParser.getChannel(PODCAST_URL)
            channel.toDomain()
        }
    }
}

private const val PODCAST_URL = "https://anchor.fm/s/74bc02a4/podcast/rss"
