package soy.gabimoreno.data.remote.mapper

import soy.gabimoreno.data.local.audiocourses.model.AudioCourseItemDbModel
import soy.gabimoreno.data.remote.mapper.category.toCategory
import soy.gabimoreno.data.remote.mapper.category.toSubcategory
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.data.remote.model.course.CourseApiModel
import soy.gabimoreno.domain.model.audio.Saga
import soy.gabimoreno.domain.model.content.AudioCourse
import soy.gabimoreno.domain.model.content.AudioCourseItem
import soy.gabimoreno.domain.model.content.findAuthorDisplayNameById

fun List<CourseApiModel>.toDomain(): List<AudioCourse> = map { it.toDomain() }

fun CourseApiModel.toDomain(): AudioCourse {
    val excerpt = excerptApiModel.rendered
    val content = contentApiModel.rendered
    return AudioCourse(
        id = id.toString(),
        title = titleApiModel.rendered,
        description = content,
        excerpt = excerpt,
        category = categoryIds.toCategory() ?: Category.AUDIO_COURSES,
        url = url,
        saga = Saga(
            author = findAuthorDisplayNameById(authorId) ?: UNKNOWN_AUTHOR_DISPLAY_NAME,
            categoryIds.toSubcategory()?.title ?: Category.AUDIO_COURSES.title
        ),
        pubDateMillis = dateString.toMillis() ?: EMPTY_PUB_DATE_MILLIS,
        videoUrl = content.extractLoomUrl() ?: "",
        thumbnailUrl = yoastHeadJsonApiModel.ogImage[0].url,
        audioLengthInSeconds = EMPTY_AUDIO_LENGTH_IN_SECONDS,
        audios = content.extractAudioItems(id.toString()),
        isPurchased = !content.isRestrictedByRcp(),
    )
}

fun List<AudioCourseItemDbModel>.toAudioCourseMapper(): List<AudioCourseItem> = map {
    val audioCourseList = mutableListOf<AudioCourseItem>()

    this.forEach { audioCourse ->
        audioCourseList.add(
            AudioCourseItem(
                id = audioCourse.id,
                title = audioCourse.title,
                url = audioCourse.url,
            )
        )
    }

    return audioCourseList
}

internal fun AudioCourseItem.toAudioCourseItemDbModelMapper(idAudioCourse: String): AudioCourseItemDbModel =
    AudioCourseItemDbModel(
        id = id,
        title = title,
        url = url,
        idAudioCourse = idAudioCourse,
    )

internal fun String.isRestrictedByRcp(): Boolean {
    val isRestrictedByRcp =
        Regex("""class=["'].*?\brcp_restricted\b.*?["']""").containsMatchIn(this)
    return isRestrictedByRcp
}

internal fun String.extractLoomUrl(): String? {
    val regex = Regex("""<iframe[^>]+src=["']([^"']*loom\.com[^"']*)["']""")
    return regex.find(this)?.groupValues?.getOrNull(1)
}

internal fun String.extractAudioItems(idAudioCourse: String): List<AudioCourseItem> {
    val regex = Regex("title\"\\s*:\\s*\"(.*?)\".*?mp3\"\\s*:\\s*\"(https:\\\\/\\\\/.*?\\.mp3)\"")
    var counter = 0
    return regex.findAll(this)
        .map { match ->
            val title = match.groupValues[1]
            val url = match.groupValues[2].replace("\\/", "/")
            AudioCourseItem(id = "$idAudioCourse-${counter++}", title = title, url = url)
        }
        .distinctBy { it.url }
        .toList()
}
