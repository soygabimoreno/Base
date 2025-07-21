package soy.gabimoreno.domain.repository.podcast

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.data.remote.datasource.podcast.PodcastDatasource
import soy.gabimoreno.di.data.PodcastUrl
import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.domain.model.podcast.EpisodesWrapper
import javax.inject.Singleton

@Singleton
class RemotePodcastRepository(
    private val podcastDatasource: PodcastDatasource,
    private val podcastUrl: PodcastUrl,
) : PodcastRepository {
    override fun getEpisodesStream(): Either<Throwable, Flow<List<Episode>>> =
        podcastDatasource.getEpisodesStream(podcastUrl)

    override suspend fun getEpisodes(): Either<Throwable, EpisodesWrapper> =
        podcastDatasource.getEpisodes(podcastUrl)
}
