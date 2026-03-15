package soy.gabimoreno.data.local.podcast

import com.google.common.annotations.VisibleForTesting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import soy.gabimoreno.data.local.ApplicationDatabase
import soy.gabimoreno.data.local.mapper.toEpisode
import soy.gabimoreno.data.local.mapper.toSeniorDbModel
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.model.podcast.Episode
import javax.inject.Inject

class LocalSeniorAudioDataSource
    @Inject
    constructor(
        gabiMorenoDatabase: ApplicationDatabase,
        @param:IO private val dispatcher: CoroutineDispatcher,
    ) {
        @VisibleForTesting
        val seniorDbModelDao = gabiMorenoDatabase.seniorDbModelDao()

        suspend fun isEmpty(): Boolean =
            withContext(dispatcher) {
                seniorDbModelDao.count() <= 0
            }

        suspend fun getTotalSeniorAudios(): Int =
            withContext(dispatcher) {
                seniorDbModelDao.count()
            }

        fun getseniorAudios(): Flow<List<Episode>> =
            seniorDbModelDao.getSeniorDbModels().map { podcastDbModels ->
                podcastDbModels.map { podcastDbModel ->
                    podcastDbModel.toEpisode()
                }
            }

        suspend fun getPodcastById(id: String): Episode? =
            withContext(dispatcher) {
                seniorDbModelDao.getSeniorDbModelById(id)?.toEpisode()
            }

        suspend fun upsertPodcasts(podcasts: List<Episode>) =
            withContext(dispatcher) {
                seniorDbModelDao.upsertSeniorDbModels(podcasts.map { it.toSeniorDbModel() })
            }

        suspend fun reset() =
            withContext(dispatcher) {
                seniorDbModelDao.deleteAllSeniorDbModels()
            }
    }
