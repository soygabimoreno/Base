package soy.gabimoreno.data.local.mapper

import soy.gabimoreno.data.local.audiocourse.model.AudioCourseDbModel
import soy.gabimoreno.domain.model.content.AudioCourse

fun AudioCourse.toAudioCourseDbModelMapper() =
    AudioCourseDbModel(
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
