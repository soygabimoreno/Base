package soy.gabimoreno.data.local.senioraudio

import com.google.common.annotations.VisibleForTesting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import soy.gabimoreno.data.local.ApplicationDatabase
import soy.gabimoreno.data.local.mapper.toSeniorAudio
import soy.gabimoreno.data.local.mapper.toSeniorDbModel
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.model.content.SeniorAudio
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

        fun getSeniorAudios(): Flow<List<SeniorAudio>> =
            seniorDbModelDao.getSeniorDbModels().map { podcastDbModels ->
                podcastDbModels.map { podcastDbModel ->
                    podcastDbModel.toSeniorAudio()
                }
            }

        suspend fun getSeniorAudioById(id: String): SeniorAudio? =
            withContext(dispatcher) {
                seniorDbModelDao.getSeniorDbModelById(id)?.toSeniorAudio()
            }

        suspend fun upsertSeniorAudios(seniorAudios: List<SeniorAudio>) =
            withContext(dispatcher) {
                seniorDbModelDao.upsertSeniorDbModels(seniorAudios.map { it.toSeniorDbModel() })
            }

        suspend fun reset() =
            withContext(dispatcher) {
                seniorDbModelDao.deleteAllSeniorDbModels()
            }
    }
