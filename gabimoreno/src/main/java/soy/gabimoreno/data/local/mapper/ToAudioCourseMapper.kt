package soy.gabimoreno.data.local.mapper

import soy.gabimoreno.data.local.audiocourse.model.AudioCourseDbModel
import soy.gabimoreno.data.local.audiocourse.model.AudioCourseItemDbModel
import soy.gabimoreno.data.local.audiocourse.model.AudioCourseWithItems
import soy.gabimoreno.domain.model.content.AudioCourse
import soy.gabimoreno.domain.model.content.AudioCourseItem
import soy.gabimoreno.domain.model.content.PlaylistAudioItem

fun AudioCourseDbModel.toAudioCourseMapper() =
    AudioCourse(
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
        isPurchased,
    )

fun AudioCourseItemDbModel.toAudioCourseItem(): AudioCourseItem =
    AudioCourseItem(
        id = id,
        title = title,
        url = url,
        hasBeenListened = hasBeenListened,
        markedAsFavorite = markedAsFavorite,
    )

fun AudioCourseWithItems.toAudioCourse(): AudioCourse =
    AudioCourse(
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
        audios = audios.map { it.toAudioCourseItem() },
    )

fun AudioCourseItemDbModel.toPlaylistAudioItem(
    audioCourseDbModel: AudioCourseDbModel,
    position: Int,
): PlaylistAudioItem =
    PlaylistAudioItem(
        id = id,
        title = title,
        description = audioCourseDbModel.title,
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
        position = position,
        playlistItemId = null,
        markedAsFavorite = markedAsFavorite,
    )

fun AudioCourseItem.toPlaylistAudioItem(
    audioCourse: AudioCourse,
    position: Int,
): PlaylistAudioItem =
    PlaylistAudioItem(
        id = id,
        title = title,
        description = audioCourse.title,
        saga = audioCourse.saga,
        url = url,
        audioUrl = url,
        imageUrl = audioCourse.thumbnailUrl,
        thumbnailUrl = audioCourse.thumbnailUrl,
        pubDateMillis = audioCourse.pubDateMillis,
        audioLengthInSeconds = audioCourse.audioLengthInSeconds,
        hasBeenListened = hasBeenListened,
        category = audioCourse.category,
        excerpt = audioCourse.excerpt,
        position = position,
        playlistItemId = null,
        markedAsFavorite = markedAsFavorite,
    )
