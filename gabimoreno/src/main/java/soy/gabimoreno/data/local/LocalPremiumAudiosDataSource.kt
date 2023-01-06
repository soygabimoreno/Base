package soy.gabimoreno.data.local

import com.google.common.annotations.VisibleForTesting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import soy.gabimoreno.data.local.mapper.toPremiumAudio
import soy.gabimoreno.data.local.mapper.toPremiumAudioDbModel
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.model.content.PremiumAudio
import javax.inject.Inject

class LocalPremiumAudiosDataSource @Inject constructor(
    gabiMorenoDatabase: GabiMorenoDatabase,
    @IO private val dispatcher: CoroutineDispatcher,
) {

    @VisibleForTesting
    val premiumAudioDbModelDao = gabiMorenoDatabase.premiumAudioDbModelDao()

    suspend fun isEmpty(): Boolean = withContext(dispatcher) {
        premiumAudioDbModelDao.count() <= 0
    }

    suspend fun savePremiumAudios(premiumAudios: List<PremiumAudio>) = withContext(dispatcher) {
        premiumAudioDbModelDao.insertPremiumAudioDbModels(
            premiumAudios.map {
                it.toPremiumAudioDbModel()
            },
        )
    }

    suspend fun getPremiumAudios(): List<PremiumAudio> = withContext(dispatcher) {
        premiumAudioDbModelDao.getPremiumAudioDbModels().map {
            it.toPremiumAudio()
        }
    }

    suspend fun reset() = withContext(dispatcher) {
        premiumAudioDbModelDao.deleteAllPremiumAudioDbModels()
    }
}
