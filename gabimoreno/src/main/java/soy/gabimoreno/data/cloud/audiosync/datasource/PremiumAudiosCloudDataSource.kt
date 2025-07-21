package soy.gabimoreno.data.cloud.audiosync.datasource

import soy.gabimoreno.data.cloud.audiosync.response.SyncablePremiumAudioResponse
import javax.inject.Inject

class PremiumAudiosCloudDataSource
    @Inject
    constructor(
        private val cloudDataSource: CloudDataSource<SyncablePremiumAudioResponse>,
    ) {
        suspend fun getPremiumAudioItems(email: String): List<SyncablePremiumAudioResponse> =
            cloudDataSource.getAudioItems(email, PREMIUM_AUDIOS_PATH)

        fun upsertPremiumAudioItemFields(
            email: String,
            itemId: String,
            updates: Map<String, Any>,
        ) {
            cloudDataSource.upsertAudioItemFields(email, PREMIUM_AUDIOS_PATH, itemId, updates)
        }

        suspend fun batchUpdateFieldsForAllPremiumAudioItems(
            email: String,
            updates: Map<String, Any>,
        ) {
            cloudDataSource.batchUpdateFieldsForAllItems<SyncablePremiumAudioResponse>(
                email,
                PREMIUM_AUDIOS_PATH,
                updates,
            )
        }
    }
