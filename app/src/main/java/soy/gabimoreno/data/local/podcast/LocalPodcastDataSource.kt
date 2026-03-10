package soy.gabimoreno.data.local.podcast

import com.google.common.annotations.VisibleForTesting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import soy.gabimoreno.data.local.ApplicationDatabase
import soy.gabimoreno.data.local.mapper.toEpisode
import soy.gabimoreno.data.local.mapper.toPodcastDbModel
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.model.podcast.Episode
import javax.inject.Inject

class LocalPodcastDataSource
    @Inject
    constructor(
        gabiMorenoDatabase: ApplicationDatabase,
        @param:IO private val dispatcher: CoroutineDispatcher,
    ) {
        @VisibleForTesting
        val podcastDbModelDao = gabiMorenoDatabase.podcastDbModelDao()

        suspend fun isEmpty(): Boolean =
            withContext(dispatcher) {
                podcastDbModelDao.count() <= 0
            }

        suspend fun getTotalPodcasts(): Int =
            withContext(dispatcher) {
                podcastDbModelDao.count()
            }

        fun getPodcasts(): Flow<List<Episode>> =
            podcastDbModelDao.getPodcastDbModels().map { podcastDbModels ->
                podcastDbModels.map { podcastDbModel ->
                    podcastDbModel.toEpisode()
                }
            }

        suspend fun getPodcastById(id: String): Episode? =
            withContext(dispatcher) {
                podcastDbModelDao.getPodcastDbModelById(id)?.toEpisode()
            }

        suspend fun upsertPodcasts(podcasts: List<Episode>) =
            withContext(dispatcher) {
                podcastDbModelDao.upsertPodcastDbModels(podcasts.map { it.toPodcastDbModel() })
            }

        suspend fun reset() =
            withContext(dispatcher) {
                podcastDbModelDao.deleteAllPodcastDbModels()
            }

        suspend fun updateHasBeenListened(
            id: String,
            hasBeenListened: Boolean,
        ) = withContext(dispatcher) {
            podcastDbModelDao.updateHasBeenListened(id, hasBeenListened)
        }

        suspend fun updateMarkedAsFavorite(
            id: String,
            markedAsFavorite: Boolean,
        ) = withContext(dispatcher) {
            podcastDbModelDao.updateMarkedAsFavorite(id, markedAsFavorite)
        }

        suspend fun markAllPodcastAsUnlistened() =
            withContext(dispatcher) {
                podcastDbModelDao.markAllPodcastAsUnlistened()
            }
    }
