package soy.gabimoreno.data.network.mapper

import com.prof.rssparser.*
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import soy.gabimoreno.domain.model.GABI_MORENO_WEB_BASE_URL
import java.util.*

class PodcastMapperKtTest {

    @Test
    fun `GIVEN a Channel WHEN toDomain THEN get the expected PodcastSearch`() {
        val channel = buildChannel()
        val numberOfEpisodes = channel.articles.size.toLong()

        val result = channel.toDomain()

        result.count shouldBe numberOfEpisodes
        result.total shouldBe numberOfEpisodes

        result.episodes.forEachIndexed { index, episode ->
            episode.id shouldBeEqualTo EPISODE_ID
            episode.url shouldBeEqualTo EPISODE_URL
            episode.audioUrl shouldBe EPISODE_AUDIO_URL
            episode.pubDateMillis shouldBeEqualTo Date(EPISODE_PUB_DATE).time
            episode.title shouldBe EPISODE_TITLE

            when (index) {
                0 -> {
                    episode.imageUrl shouldBe EPISODE_IMAGE_URL
                    episode.thumbnailUrl shouldBe EPISODE_IMAGE_URL
                    episode.audioLengthInSeconds shouldBeEqualTo EPISODE_AUDIO_LENGTH_IN_SECONDS
                }
                1 -> {
                    episode.imageUrl shouldBe EPISODE_IMAGE_URL
                    episode.thumbnailUrl shouldBe EPISODE_IMAGE_URL
                    episode.audioLengthInSeconds shouldBeEqualTo EPISODE_AUDIO_LENGTH_IN_TIME_FORMATTED_IN_SECONDS
                }
                2 -> {
                    episode.imageUrl shouldBe EPISODE_EMPTY_IMAGE_URL
                    episode.thumbnailUrl shouldBe EPISODE_EMPTY_IMAGE_URL
                    episode.audioLengthInSeconds shouldBeEqualTo EPISODE_AUDIO_LENGTH_DEFAULT_DURATION
                }
                3 -> {
                    episode.imageUrl shouldBe EPISODE_IMAGE_URL
                    episode.thumbnailUrl shouldBe EPISODE_IMAGE_URL
                    episode.audioLengthInSeconds shouldBeEqualTo EPISODE_AUDIO_LENGTH_DEFAULT_DURATION
                }
            }

            episode.description shouldBeEqualTo EPISODE_DESCRIPTION

            val saga = episode.saga
            saga.title shouldBe PODCAST_TITLE
            saga.author shouldBeEqualTo PODCAST_TITLE.uppercase()
        }
    }

    private fun buildChannel(): Channel {
        return Channel(
            title = PODCAST_TITLE,
            link = "link",
            description = "description",
            image = Image(
                "title",
                "url",
                "link",
                "description"
            ),
            lastBuildDate = "lastBuildDate",
            updatePeriod = "updatePeriod",
            articles = listOf(
                buildArticle(EPISODE_AUDIO_LENGTH_IN_SECONDS.toString()),
                run {
                    val duration = EPISODE_AUDIO_LENGTH_IN_TIME_FORMATTED
                    buildArticle(duration).copy(
                        itunesArticleData = buildItunesArticleData(duration).copy(
                            episode = null
                        )
                    )
                },
                buildArticle(EPISODE_AUDIO_LENGTH_IN_SECONDS.toString()).copy(
                    itunesArticleData = null
                ),
                buildArticle(EPISODE_AUDIO_LENGTH_NULL)
            ),
            itunesChannelData = ItunesChannelData(
                author = "author",
                categories = listOf("categories"),
                duration = "2000",
                explicit = "explicit",
                image = "channelImage",
                keywords = listOf("keywords"),
                newsFeedUrl = "newsFeedUrl",
                owner = ItunesOwner(
                    "name",
                    "email"
                ),
                subtitle = "subtitle",
                summary = "summary",
                type = "type"
            )
        )
    }

    private fun buildArticle(duration: String?) = Article(
        guid = "$EPISODE_ID$IVOOX_URL",
        title = EPISODE_TITLE,
        author = "author",
        link = "link",
        pubDate = EPISODE_PUB_DATE,
        description = "$EPISODE_DESCRIPTION$ANCHOR_MESSAGE",
        content = "content",
        image = null,
        audio = EPISODE_AUDIO_URL,
        video = "video",
        sourceName = "sourceName",
        sourceUrl = "sourceUrl",
        categories = listOf("categories"),
        itunesArticleData = buildItunesArticleData(duration)
    )

    private fun buildItunesArticleData(duration: String?) = ItunesArticleData(
        author = "author",
        duration = duration,
        episode = EPISODE_NUMBER,
        episodeType = "episodeType",
        explicit = "explicit",
        image = EPISODE_IMAGE_URL,
        keywords = listOf("keywords"),
        subtitle = "subtitle",
        summary = "summary"
    )
}

private const val EPISODE_ID = "guid"
private const val EPISODE_NUMBER = "1234"
private const val EPISODE_URL = "$GABI_MORENO_WEB_BASE_URL/$EPISODE_NUMBER"
private const val EPISODE_AUDIO_URL = "audio"
private const val EPISODE_IMAGE_URL = "image"
private const val EPISODE_TITLE = "$EPISODE_NUMBER. Title"
private const val EPISODE_AUDIO_LENGTH_IN_SECONDS = 896
private const val EPISODE_AUDIO_LENGTH_IN_TIME_FORMATTED = "01:51:15"
private const val EPISODE_AUDIO_LENGTH_IN_TIME_FORMATTED_IN_SECONDS = 1 * 60 * 60 + 51 * 60 + 15
private val EPISODE_AUDIO_LENGTH_NULL = null
private const val EPISODE_PUB_DATE = "Mon, 27 Jun 2022 04:00:33 GMT"
private const val EPISODE_DESCRIPTION = "description"

private const val PODCAST_TITLE = "podcastTitle"
