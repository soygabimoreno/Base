package soy.gabimoreno.domain.repository.podcast

import arrow.core.Either
import soy.gabimoreno.data.remote.datasource.podcast.PodcastDatasource
import soy.gabimoreno.domain.model.podcast.EpisodesWrapper
import javax.inject.Singleton

@Singleton
class RemotePodcastRepository(
    private val podcastDatasource: PodcastDatasource,
) : PodcastRepository {

    override suspend fun getEpisodes(): Either<Throwable, EpisodesWrapper> {
        return podcastDatasource.getEpisodes(PODCAST_URL)
    }
}

private const val PODCAST_URL = "https://anchor.fm/s/74bc02a4/podcast/rss"
