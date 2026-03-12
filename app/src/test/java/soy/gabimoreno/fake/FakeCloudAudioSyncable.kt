package soy.gabimoreno.fake

import soy.gabimoreno.data.cloud.audiosync.response.SyncableAudioCourseResponse
import soy.gabimoreno.data.cloud.audiosync.response.SyncablePodcastResponse
import soy.gabimoreno.data.cloud.audiosync.response.SyncablePremiumAudioResponse
import soy.gabimoreno.domain.model.content.AudioCourse

fun buildCloudAudiocourseResponseList(hasBeenListened: Boolean = false): List<SyncableAudioCourseResponse> =
    (1..3).map { index ->
        buildCloudAudiocourseResponse("1-$index", hasBeenListened)
    }

fun buildCloudAudiocourseResponse(
    id: String,
    hasBeenListened: Boolean,
) = SyncableAudioCourseResponse(
    id = id,
    hasBeenListened = hasBeenListened,
    markedAsFavorite = false,
)

fun buildCloudAudioCourseResponses(
    remoteCourses: List<AudioCourse>,
    hasBeenListened: Boolean = false,
    markedAsFavorite: Boolean = false,
): List<SyncableAudioCourseResponse> =
    remoteCourses.flatMap { audioCourse ->
        audioCourse.audios.map { audio ->
            SyncableAudioCourseResponse(
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

fun buildCloudPodcastResponse(
    id: String,
    hasBeenListened: Boolean,
) = SyncablePodcastResponse(
    id = id,
    hasBeenListened = hasBeenListened,
    markedAsFavorite = false,
)
