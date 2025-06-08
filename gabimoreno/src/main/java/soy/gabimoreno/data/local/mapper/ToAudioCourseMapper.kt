package soy.gabimoreno.data.local.mapper

import soy.gabimoreno.data.local.audiocourse.model.AudioCourseDbModel
import soy.gabimoreno.data.local.audiocourse.model.AudioCourseItemDbModel
import soy.gabimoreno.data.local.audiocourse.model.AudioCourseWithItems
import soy.gabimoreno.domain.model.content.AudioCourse
import soy.gabimoreno.domain.model.content.AudioCourseItem
import soy.gabimoreno.domain.model.content.PlaylistAudioItem

fun AudioCourseDbModel.toAudioCourseMapper() = AudioCourse(
    id,
    title,
    description,
    excerpt,
    saga,
    url,
    videoUrl,
    thumbnailUrl,
    pubDateMillis,
    audioLengthInSeconds,
    category,
    isPurchased
)

fun AudioCourseItemDbModel.toAudioCourseItem(): AudioCourseItem {
    return AudioCourseItem(
        id = id,
        title = title,
        url = url,
        hasBeenListened = hasBeenListened
    )
}

fun AudioCourseWithItems.toAudioCourse(): AudioCourse {
    return AudioCourse(
        id = course.id,
        title = course.title,
        description = course.description,
        excerpt = course.excerpt,
        saga = course.saga,
        url = course.url,
        videoUrl = course.videoUrl,
        thumbnailUrl = course.thumbnailUrl,
        pubDateMillis = course.pubDateMillis,
        audioLengthInSeconds = course.audioLengthInSeconds,
        category = course.category,
        isPurchased = course.isPurchased,
        audios = audios.map { it.toAudioCourseItem() }
    )
}

fun AudioCourseItemDbModel.toPlaylistAudioItem(
    audioCourseDbModel: AudioCourseDbModel,
    position: Int,
): PlaylistAudioItem {
    return PlaylistAudioItem(
        id = id,
        title = title,
        description = audioCourseDbModel.description,
        saga = audioCourseDbModel.saga,
        url = url,
        audioUrl = url,
        imageUrl = audioCourseDbModel.thumbnailUrl,
        thumbnailUrl = audioCourseDbModel.thumbnailUrl,
        pubDateMillis = audioCourseDbModel.pubDateMillis,
        audioLengthInSeconds = audioCourseDbModel.audioLengthInSeconds,
        hasBeenListened = hasBeenListened,
        category = audioCourseDbModel.category,
        excerpt = audioCourseDbModel.excerpt,
        position = position
    )
}
