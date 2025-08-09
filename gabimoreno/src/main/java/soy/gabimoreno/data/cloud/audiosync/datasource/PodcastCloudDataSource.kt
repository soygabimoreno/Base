package soy.gabimoreno.data.cloud.audiosync.datasource

import soy.gabimoreno.data.cloud.audiosync.response.SyncablePodcastResponse
import javax.inject.Inject

class PodcastCloudDataSource
    @Inject
    constructor(
        private val cloudDataSource: CloudDataSource<SyncablePodcastResponse>,
    ) {
        suspend fun getPodcastItems(email: String): List<SyncablePodcastResponse> =
            cloudDataSource.getAudioItems(email, PODCAST_PATH)

        fun upsertPodcastFields(
            email: String,
            itemId: String,
            updates: Map<String, Any>,
        ) {
            cloudDataSource.upsertAudioItemFields(email, PODCAST_PATH, itemId, updates)
        }

        suspend fun batchUpdateFieldsForAllPodcastItems(
            email: String,
            updates: Map<String, Any>,
        ) {
            cloudDataSource.batchUpdateFieldsForAllItems<SyncablePodcastResponse>(
                email,
                PODCAST_PATH,
                updates,
            )
        }
    }
