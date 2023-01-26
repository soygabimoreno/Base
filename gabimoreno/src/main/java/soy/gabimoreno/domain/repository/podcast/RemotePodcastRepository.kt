package soy.gabimoreno.domain.repository.podcast

import arrow.core.Either
import soy.gabimoreno.data.remote.datasource.podcast.PodcastDatasource
import soy.gabimoreno.di.data.PodcastUrl
import soy.gabimoreno.domain.model.podcast.EpisodesWrapper
import javax.inject.Singleton

@Singleton
class RemotePodcastRepository(
    private val podcastDatasource: PodcastDatasource,
    private val podcastUrl: PodcastUrl,
) : PodcastRepository {

    override suspend fun getEpisodes(): Either<Throwable, EpisodesWrapper> {
        return podcastDatasource.getEpisodes(podcastUrl)
    }
}
