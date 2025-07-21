package soy.gabimoreno.data.cloud.audiosync.datasource

import soy.gabimoreno.data.cloud.audiosync.response.SyncableAudiocourseResponse
import javax.inject.Inject

class AudioCoursesCloudDataSource
    @Inject
    constructor(
        private val cloudDataSource: CloudDataSource<SyncableAudiocourseResponse>,
    ) {
        suspend fun getAudioCoursesItems(email: String): List<SyncableAudiocourseResponse> =
            cloudDataSource.getAudioItems(email, AUDIOCOURSES_PATH)

        fun upsertAudioCourseItemFields(
            email: String,
            itemId: String,
            updates: Map<String, Any>,
        ) {
            cloudDataSource.upsertAudioItemFields(email, AUDIOCOURSES_PATH, itemId, updates)
        }

        suspend fun batchUpdateFieldsForAllAudioCoursesItems(
            email: String,
            updates: Map<String, Any>,
        ) {
            cloudDataSource.batchUpdateFieldsForAllItems<SyncableAudiocourseResponse>(
                email,
                AUDIOCOURSES_PATH,
                updates,
            )
        }
    }
