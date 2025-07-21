package soy.gabimoreno.data.local.premiumaudio

import androidx.paging.PagingSource
import com.google.common.annotations.VisibleForTesting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import soy.gabimoreno.data.local.ApplicationDatabase
import soy.gabimoreno.data.local.mapper.toPremiumAudio
import soy.gabimoreno.data.local.mapper.toPremiumAudioDbModel
import soy.gabimoreno.di.IO
import soy.gabimoreno.domain.model.content.PremiumAudio
import javax.inject.Inject

class LocalPremiumAudiosDataSource
    @Inject
    constructor(
        gabiMorenoDatabase: ApplicationDatabase,
        @IO private val dispatcher: CoroutineDispatcher,
    ) {
        @VisibleForTesting
        val premiumAudioDbModelDao = gabiMorenoDatabase.premiumAudioDbModelDao()

        suspend fun isEmpty(): Boolean =
            withContext(dispatcher) {
                premiumAudioDbModelDao.count() <= 0
            }

        suspend fun getTotalPremiumAudios(): Int =
            withContext(dispatcher) {
                premiumAudioDbModelDao.count()
            }

        suspend fun savePremiumAudios(premiumAudios: List<PremiumAudio>) =
            withContext(dispatcher) {
                premiumAudioDbModelDao.upsertPremiumAudioDbModels(
                    premiumAudios.map {
                        it.toPremiumAudioDbModel()
                    },
                )
            }

        suspend fun updateHasBeenListened(
            id: String,
            hasBeenListened: Boolean,
        ) = withContext(dispatcher) {
            premiumAudioDbModelDao.updateHasBeenListened(id, hasBeenListened)
        }

        suspend fun markAllPremiumAudiosAsUnlistened() =
            withContext(dispatcher) {
                premiumAudioDbModelDao.markAllPremiumAudiosAsUnlistened()
            }

        suspend fun getPremiumAudios(): List<PremiumAudio> =
            withContext(dispatcher) {
                premiumAudioDbModelDao.getPremiumAudioDbModels().map {
                    it.toPremiumAudio()
                }
            }

        suspend fun getAllFavoritePremiumAudios(): List<PremiumAudioDbModel> =
            withContext(dispatcher) {
                premiumAudioDbModelDao.getAllFavoriteAudioPremiumAudioDbModels()
            }

        suspend fun updateMarkedAsFavorite(
            id: String,
            markedAsFavorite: Boolean,
        ) = withContext(dispatcher) {
            premiumAudioDbModelDao.updateMarkedAsFavorite(
                id = id,
                markedAsFavorite = markedAsFavorite,
            )
        }

        fun getPremiumAudiosPagingSource(): PagingSource<Int, PremiumAudioDbModel> =
            premiumAudioDbModelDao.getPremiumAudioDbModelsPagingSource()

        suspend fun getPremiumAudioById(id: String): PremiumAudio? =
            withContext(dispatcher) {
                premiumAudioDbModelDao.getPremiumAudioDbModelById(id)?.toPremiumAudio()
            }

        suspend fun reset() =
            withContext(dispatcher) {
                premiumAudioDbModelDao.deleteAllPremiumAudioDbModels()
            }
    }
