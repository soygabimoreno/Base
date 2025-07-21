package soy.gabimoreno.fake

import soy.gabimoreno.data.cloud.audiosync.response.SyncableAudiocourseResponse
import soy.gabimoreno.data.cloud.audiosync.response.SyncablePremiumAudioResponse
import soy.gabimoreno.domain.model.content.AudioCourse

fun buildCloudAudiocourseResponseList(hasBeenListened: Boolean = false): List<SyncableAudiocourseResponse> =
    (1..3).map { index ->
        buildCloudAudiocourseResponse("1-$index", hasBeenListened)
    }

fun buildCloudAudiocourseResponse(
    id: String,
    hasBeenListened: Boolean,
) = SyncableAudiocourseResponse(
    id = id,
    hasBeenListened = hasBeenListened,
    markedAsFavorite = false,
)

fun buildCloudAudioCourseResponses(
    remoteCourses: List<AudioCourse>,
    hasBeenListened: Boolean = false,
    markedAsFavorite: Boolean = false,
): List<SyncableAudiocourseResponse> =
    remoteCourses.flatMap { course ->
        course.audios.map { audio ->
            SyncableAudiocourseResponse(
                id = audio.id,
                hasBeenListened = hasBeenListened,
                markedAsFavorite = markedAsFavorite,
            )
        }
    }

fun buildCloudPremiumAudiosResponseList(hasBeenListened: Boolean = false): List<SyncablePremiumAudioResponse> =
    (1..3).map { index ->
        buildCloudPremiumAudiosResponse(index.toString(), hasBeenListened)
    }

fun buildCloudPremiumAudiosResponse(
    id: String,
    hasBeenListened: Boolean,
) = SyncablePremiumAudioResponse(
    id = id,
    hasBeenListened = hasBeenListened,
    markedAsFavorite = false,
)
