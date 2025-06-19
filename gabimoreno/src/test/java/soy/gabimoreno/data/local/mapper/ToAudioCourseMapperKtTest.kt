package soy.gabimoreno.data.local.mapper

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import soy.gabimoreno.fake.buildAudioCourseDbModel
import soy.gabimoreno.fake.buildAudioCourseItemDbModel
import soy.gabimoreno.fake.buildAudioCourseWithItems


class ToAudioCourseMapperKtTest {

    @Test
    fun `GIVEN an AudioCourseDbModel WHEN toAudioCourse THEN get the expected AudioCourse`() {
        val audioCourseDbModel = buildAudioCourseDbModel()

        val result = audioCourseDbModel.toAudioCourseMapper()

        result.id shouldBe audioCourseDbModel.id
        result.title shouldBe audioCourseDbModel.title
        result.description shouldBe audioCourseDbModel.description
        result.excerpt shouldBeEqualTo audioCourseDbModel.excerpt
        result.saga shouldBe audioCourseDbModel.saga
        result.url shouldBe audioCourseDbModel.url
        result.videoUrl shouldBe audioCourseDbModel.videoUrl
        result.thumbnailUrl shouldBe audioCourseDbModel.thumbnailUrl
        result.pubDateMillis shouldBe audioCourseDbModel.pubDateMillis
        result.audioLengthInSeconds shouldBeEqualTo audioCourseDbModel.audioLengthInSeconds
        result.category shouldBeEqualTo audioCourseDbModel.category
        result.isPurchased shouldBe audioCourseDbModel.isPurchased
    }

    @Test
    fun `GIVEN AudioCourseItemDbModel WHEN toAudioCourseItem THEN mapped correctly`() {
        val audioCourseItemDbModel = buildAudioCourseItemDbModel()

        val result = audioCourseItemDbModel.toAudioCourseItem()

        result.id shouldBe audioCourseItemDbModel.id
        result.title shouldBe audioCourseItemDbModel.title
        result.url shouldBe audioCourseItemDbModel.url
    }

    @Test
    fun `GIVEN AudioCourseWithItems WHEN toAudioCourse THEN mapped correctly`() {
        val audioCourseWithItems = buildAudioCourseWithItems()

        val result = audioCourseWithItems.toAudioCourse()

        result.id shouldBe audioCourseWithItems.course.id
        result.title shouldBe audioCourseWithItems.course.title
        result.description shouldBe audioCourseWithItems.course.description
        result.excerpt shouldBeEqualTo audioCourseWithItems.course.excerpt
        result.saga shouldBe audioCourseWithItems.course.saga
        result.url shouldBe audioCourseWithItems.course.url
        result.videoUrl shouldBe audioCourseWithItems.course.videoUrl
        result.thumbnailUrl shouldBe audioCourseWithItems.course.thumbnailUrl
        result.pubDateMillis shouldBe audioCourseWithItems.course.pubDateMillis
        result.audioLengthInSeconds shouldBeEqualTo audioCourseWithItems.course.audioLengthInSeconds
        result.category shouldBeEqualTo audioCourseWithItems.course.category
        result.isPurchased shouldBe audioCourseWithItems.course.isPurchased
        result.audios.size shouldBe audioCourseWithItems.audios.size
        result.audios[0].id shouldBe audioCourseWithItems.audios[0].id
        result.audios[0].title shouldBe audioCourseWithItems.audios[0].title
        result.audios[0].url shouldBe audioCourseWithItems.audios[0].url
        result.audios[1].id shouldBe audioCourseWithItems.audios[1].id
        result.audios[1].title shouldBe audioCourseWithItems.audios[1].title
        result.audios[1].url shouldBe audioCourseWithItems.audios[1].url
    }

    @Test
    fun `GIVEN valid models WHEN toPlaylistAudioItem THEN returns correct PlaylistAudioItem`() {
        val audioCourseDbModel = buildAudioCourseDbModel()
        val audioCourseItemDbModel =
            buildAudioCourseItemDbModel(idAudioCourse = audioCourseDbModel.id)
        val position = 2

        val result = audioCourseItemDbModel.toPlaylistAudioItem(audioCourseDbModel, position)

        result.id shouldBeEqualTo audioCourseItemDbModel.id
        result.title shouldBeEqualTo audioCourseItemDbModel.title
        result.url shouldBeEqualTo audioCourseItemDbModel.url
        result.audioUrl shouldBeEqualTo audioCourseItemDbModel.url
        result.description shouldBeEqualTo audioCourseDbModel.title
        result.saga shouldBeEqualTo audioCourseDbModel.saga
        result.imageUrl shouldBeEqualTo audioCourseDbModel.thumbnailUrl
        result.thumbnailUrl shouldBeEqualTo audioCourseDbModel.thumbnailUrl
        result.pubDateMillis shouldBeEqualTo audioCourseDbModel.pubDateMillis
        result.audioLengthInSeconds shouldBeEqualTo audioCourseDbModel.audioLengthInSeconds
        result.hasBeenListened shouldBeEqualTo audioCourseItemDbModel.hasBeenListened
        result.category shouldBeEqualTo audioCourseDbModel.category
        result.excerpt shouldBeEqualTo audioCourseDbModel.excerpt
        result.position shouldBeEqualTo position
    }
}
