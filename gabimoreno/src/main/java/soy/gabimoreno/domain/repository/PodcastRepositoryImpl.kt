package soy.gabimoreno.domain.repository

import arrow.core.Either
import com.prof.rssparser.Parser
import soy.gabimoreno.data.network.mapper.toDomain
import soy.gabimoreno.domain.model.PodcastSearch

class PodcastRepositoryImpl(
    private val rssParser: Parser
) : PodcastRepository {

    companion object {
        private const val TAG = "PodcastRepository"
    }

    override suspend fun getEpisodes(): Either<Throwable, PodcastSearch> {
        return Either.catch {
            val channel = rssParser.getChannel(PODCAST_URL)
            channel.toDomain()
        }
    }
}

private const val PODCAST_URL = "https://anchor.fm/s/74bc02a4/podcast/rss"
