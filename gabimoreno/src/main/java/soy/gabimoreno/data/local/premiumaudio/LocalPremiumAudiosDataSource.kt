package soy.gabimoreno.data.local.premiumaudio

import androidx.paging.PagingSource
import com.google.common.annotations.VisibleForTesting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import soy.gabimoreno.data.local.GabiMorenoDatabase
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

    suspend fun getTotalPremiumAudios(): Int = withContext(dispatcher) {
        premiumAudioDbModelDao.count()
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

    fun getPremiumAudiosPagingSource(): PagingSource<Int, PremiumAudioDbModel> {
        return premiumAudioDbModelDao.getPremiumAudioDbModelsPagingSource()
    }

    suspend fun getPremiumAudioById(id: String): PremiumAudio? = withContext(dispatcher) {
        premiumAudioDbModelDao.getPremiumAudioDbModelById(id)?.toPremiumAudio()
    }

    suspend fun reset() = withContext(dispatcher) {
        premiumAudioDbModelDao.deleteAllPremiumAudioDbModels()
    }
}
