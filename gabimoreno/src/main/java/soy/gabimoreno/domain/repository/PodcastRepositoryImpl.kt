package soy.gabimoreno.domain.repository

import com.prof.rssparser.Parser
import soy.gabimoreno.data.network.mapper.toDomain
import soy.gabimoreno.domain.model.PodcastSearch
import soy.gabimoreno.error.Failure
import soy.gabimoreno.util.Either
import soy.gabimoreno.util.left
import soy.gabimoreno.util.right

class PodcastRepositoryImpl(
    private val rssParser: Parser
) : PodcastRepository {

    companion object {
        private const val TAG = "PodcastRepository"
    }

    override suspend fun getPodcast(): Either<Failure, PodcastSearch> {
        return try {
            val channel = rssParser.getChannel(PODCAST_URL)
            val result = channel.toDomain()
            right(result)
        } catch (e: Exception) {
            left(Failure.UnexpectedFailure)
        }
    }
}

private const val PODCAST_URL = "https://anchor.fm/s/74bc02a4/podcast/rss"
