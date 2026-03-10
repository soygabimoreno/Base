package soy.gabimoreno.data.local.mapper

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import soy.gabimoreno.fake.buildAudioCourse

class ToAudioCourseDbModelMapperKtTest {
    @Test
    fun `GIVEN an AudioCourse WHEN toAudioCourseDbModel THEN get the expected AudioCourseDbModel`() {
        val audioCourse = buildAudioCourse()
        with(audioCourse) {
            val id = id
            val title = title
            val description = description
            val excerpt = excerpt
            val saga = saga
            val url = url
            val videoUrl = videoUrl
            val thumbnailUrl = thumbnailUrl
            val pubDateMillis = pubDateMillis
            val audioLengthInSeconds = audioLengthInSeconds
            val category = category
            val isPurchased = isPurchased

            val result = audioCourse.toAudioCourseDbModelMapper()

            result.id shouldBe id
            result.title shouldBe title
            result.description shouldBe description
            result.excerpt shouldBeEqualTo excerpt
            result.saga shouldBe saga
            result.url shouldBe url
            result.videoUrl shouldBe videoUrl
            result.thumbnailUrl shouldBe thumbnailUrl
            result.pubDateMillis shouldBe pubDateMillis
            result.audioLengthInSeconds shouldBeEqualTo audioLengthInSeconds
            result.category shouldBeEqualTo category
            result.isPurchased shouldBe isPurchased
        }
    }
}
