package soy.gabimoreno.data.cloud.audiosync.datasource

import soy.gabimoreno.data.cloud.audiosync.response.SyncableAudioCourseResponse
import javax.inject.Inject

class AudioCourseCloudDataSource
    @Inject
    constructor(
        private val cloudDataSource: CloudDataSource<SyncableAudioCourseResponse>,
    ) {
        suspend fun getAudioCoursesItems(email: String): List<SyncableAudioCourseResponse> =
            cloudDataSource.getAudioItems(email, AUDIO_COURSES_PATH)

        fun upsertAudioCourseItemFields(
            email: String,
            itemId: String,
            updates: Map<String, Any>,
        ) {
            cloudDataSource.upsertAudioItemFields(email, AUDIO_COURSES_PATH, itemId, updates)
        }

        suspend fun batchUpdateFieldsForAllAudioCoursesItems(
            email: String,
            updates: Map<String, Any>,
        ) {
            cloudDataSource.batchUpdateFieldsForAllItems<SyncableAudioCourseResponse>(
                email,
                AUDIO_COURSES_PATH,
                updates,
            )
        }
    }
