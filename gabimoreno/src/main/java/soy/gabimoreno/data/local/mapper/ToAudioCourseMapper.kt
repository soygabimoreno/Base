package soy.gabimoreno.data.local.mapper

import soy.gabimoreno.data.local.audiocourses.model.AudioCourseDbModel
import soy.gabimoreno.data.local.audiocourses.model.AudioCourseItemDbModel
import soy.gabimoreno.data.local.audiocourses.model.AudioCourseWithItems
import soy.gabimoreno.domain.model.audio.Saga
import soy.gabimoreno.domain.model.content.AudioCourse
import soy.gabimoreno.domain.model.content.AudioCourseItem

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
        url = url
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
