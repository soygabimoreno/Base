package soy.gabimoreno.fake

import soy.gabimoreno.data.local.audiocourses.model.AudioCourseDbModel
import soy.gabimoreno.data.local.audiocourses.model.AudioCourseItemDbModel
import soy.gabimoreno.data.local.audiocourses.model.AudioCourseWithItems
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.audio.Saga
import soy.gabimoreno.domain.model.content.AudioCourse
import soy.gabimoreno.domain.model.content.AudioCourseItem

fun buildAudioCourses() = listOf(
    buildAudioCourse().copy(id = "1"),
    buildAudioCourse().copy(id = "2"),
    buildAudioCourse().copy(id = "3")
)

fun buildAudioCourse(): AudioCourse = AudioCourse(
    id = "1",
    url = "",
    videoUrl = "",
    thumbnailUrl = "",
    pubDateMillis = 0,
    title = "This is a title",
    audioLengthInSeconds = 2700,
    description = "This is a description",
    category = Category.AUDIO_COURSES,
    excerpt = "excerpt",
    saga = Saga(author = "This is publisher", title = "This is saga title"),
    isPurchased = false,
    audios = buildAudioCourseItems()
)

fun buildAudioCourseItems() = listOf(
    buildAudioCourseItem().copy(id = "1"),
    buildAudioCourseItem().copy(id = "2"),
    buildAudioCourseItem().copy(id = "3")
)

fun buildAudioCourseItem() = AudioCourseItem(
    id = "1",
    title = "item title",
    url = "item url"
)

fun buildAudioCoursesDbModel() = listOf(
    buildAudioCourseDbModel().copy(id = "1"),
    buildAudioCourseDbModel().copy(id = "2"),
    buildAudioCourseDbModel().copy(id = "3")
)

fun buildAudioCourseDbModel() = AudioCourseDbModel(
    id = "1",
    title = "This is a title",
    description = "This is a description",
    excerpt = "excerpt",
    saga = Saga(author = "This is publisher", title = "This is saga title"),
    url = "",
    videoUrl = "",
    thumbnailUrl = "",
    pubDateMillis = 0,
    audioLengthInSeconds = 2700,
    category = Category.AUDIO_COURSES,
    isPurchased = false,
)

fun buildAudioCourseDbItems(idAudioCourse: String = "1") = listOf(
    buildAudioCourseItemDbModel(idAudioCourse).copy(id = "1-1"),
    buildAudioCourseItemDbModel(idAudioCourse).copy(id = "1-2"),
    buildAudioCourseItemDbModel(idAudioCourse).copy(id = "1-3")
)

fun buildAudioCourseItemDbModel(idAudioCourse: String = "1") = AudioCourseItemDbModel(
    id = "${idAudioCourse}-1",
    idAudioCourse = idAudioCourse,
    title = "item title",
    url = "item url"
)

fun buildAudioCourseWithItems() = AudioCourseWithItems(
    course = buildAudioCourseDbModel(),
    audios = buildAudioCourseDbItems()
)
