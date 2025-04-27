package soy.gabimoreno.data.remote.mapper

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import soy.gabimoreno.data.remote.model.course.CourseApiModel
import soy.gabimoreno.data.remote.model.course.OgImageApiModel
import soy.gabimoreno.data.remote.model.course.YoastHeadJsonApiModel
import soy.gabimoreno.data.remote.model.post.ContentApiModel
import soy.gabimoreno.data.remote.model.post.ExcerptApiModel
import soy.gabimoreno.data.remote.model.post.TitleApiModel
import soy.gabimoreno.fake.buildAudioCourseItem
import soy.gabimoreno.fake.buildAudioCourseItemDbModel

class AudioCourseMapperTest {

    @Test
    fun `GIVEN valid CourseApiModel WHEN toDomain THEN maps correctly`() {
        val apiModel = buildApiModel()

        val result = apiModel.toDomain()

        with(result) {
            id shouldBeEqualTo ID.toString()
            title shouldBeEqualTo TITLE
            excerpt shouldBeEqualTo EXCERPT
            videoUrl shouldBeEqualTo VIDEO_URL
            thumbnailUrl shouldBeEqualTo YOAST_HEAD_JSON
            audioLengthInSeconds shouldBeEqualTo EMPTY_AUDIO_LENGTH_IN_SECONDS
            isPurchased shouldBeEqualTo true
            audios.size shouldBeEqualTo 1
            audios.first().url shouldBeEqualTo "https://cdn.com/audio.mp3"
        }
    }

    @Test
    fun `GIVEN list of CourseApiModel WHEN toDomain THEN maps all elements`() {
        val model1 = buildApiModel()
        val model2 = buildApiModel(id = 5678L)

        val result = listOf(model1, model2).toDomain()

        result.size shouldBeEqualTo 2
        result[0].id shouldBeEqualTo ID.toString()
        result[1].id shouldBeEqualTo "5678"
    }

    @Test
    fun `GIVEN list of AudioCourseItemDbModel WHEN toAudioCourseMapper THEN maps correctly`() {
        val list = listOf(
            buildAudioCourseItemDbModel(),
            buildAudioCourseItemDbModel().copy(id = "1-2", url = "url2")
        )

        val result = list.toAudioCourseMapper()

        result.size shouldBeEqualTo 2
        result[0].id shouldBeEqualTo "1-1"
        result[1].url shouldBeEqualTo "url2"
    }

    @Test
    fun `GIVEN AudioCourseItem WHEN toAudioCourseItemDbModelMapper THEN maps correctly`() {
        val item = buildAudioCourseItem()

        val result = item.toAudioCourseItemDbModelMapper("1")

        result.idAudioCourse shouldBeEqualTo "1"
        result.title shouldBeEqualTo "item title"
    }

    @Test
    fun `GIVEN HTML string with rcp class WHEN isRestrictedByRcp THEN returns true`() {
        val html = """<div class="rcp_restricted">Content</div>"""

        val result = html.isRestrictedByRcp()

        result shouldBe true
    }

    @Test
    fun `GIVEN HTML without rcp class WHEN isRestrictedByRcp THEN returns false`() {
        val html = """<div>Paid content with audios</div>"""

        val result = html.isRestrictedByRcp()

        result shouldBe false
    }

    @Test
    fun `GIVEN iframe with Loom src WHEN extractLoomUrl THEN returns Loom url`() {
        val html = CONTENT.trimIndent()

        val result = html.extractLoomUrl()

        result shouldBeEqualTo VIDEO_URL
    }

    @Test
    fun `GIVEN string with audio items WHEN extractAudioItems THEN returns audio course items`() {
        val input = CONTENT.trimIndent()

        val result = input.extractAudioItems(ID.toString())

        result.size shouldBeEqualTo 1
        result[0].id shouldBeEqualTo "${ID}-0"
        result[0].title shouldBeEqualTo "Intro"
        result[0].url shouldBeEqualTo "https://cdn.com/audio.mp3"
    }

    @Test
    fun `GIVEN repeated audio urls WHEN extractAudioItems THEN returns distinct items`() {
        val input = CONTENT.trimIndent()

        val result = input.extractAudioItems("1")

        result.size shouldBeEqualTo 1
    }
}

private fun buildApiModel(id: Long = ID): CourseApiModel = CourseApiModel(
    id = id,
    titleApiModel = TitleApiModel(TITLE),
    excerptApiModel = ExcerptApiModel(EXCERPT),
    contentApiModel = ContentApiModel(CONTENT),
    categoryIds = CATEGORY_IDS,
    url = URL,
    authorId = AUTHOR_ID,
    dateString = DATE_STRING,
    yoastHeadJsonApiModel = YoastHeadJsonApiModel(listOf(OgImageApiModel(YOAST_HEAD_JSON)))
)

private const val ID = 1234L
private const val TITLE = "title"
private const val EXCERPT = "Excerpt <b>bold</b>"
private const val YOAST_HEAD_JSON = "https://img.jpg"
private const val AUTHOR_ID = 1
private val CATEGORY_IDS = listOf(1)
private const val URL = "https://example.com"
private const val DATE_STRING = "2025-09-05T12:46:23"
private const val VIDEO_URL = "https://www.loom.com/embed/abc123"
private const val CONTENT = """
    <iframe src="https://www.loom.com/embed/abc123"></iframe>
            title":"Intro","mp3":"https:\/\/cdn.com/audio.mp3"
            title":"Intro Again","mp3":"https:\/\/cdn.com/audio.mp3"
        """

