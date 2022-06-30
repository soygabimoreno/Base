package soy.gabimoreno.data.network.mapper

import com.prof.rssparser.*
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import java.util.*

class PodcastMapperKtTest {

    @Test
    fun `GIVEN a Channel WHEN toDomain THEN get the expected PodcastSearch`() {
        val channel = buildChannel()
        val numberOfEpisodes = channel.articles.size.toLong()

        val result = channel.toDomain()

        result.count shouldBe numberOfEpisodes
        result.total shouldBe numberOfEpisodes

        val episode = result.results[0]
        episode.id shouldBe "guid"
        episode.url shouldBe "https://gabimoreno.soy"
        episode.audioUrl shouldBe "audio"
        episode.imageUrl shouldBe "image"
//        episode.podcast TODO ???
        episode.thumbnailUrl shouldBe "image"
        episode.pubDateMillis shouldBeEqualTo Date(PUB_DATE).time
        episode.title shouldBe "title"
        episode.audioLengthSeconds shouldBeEqualTo AUDIO_LENGTH_IN_SECONDS
        episode.description shouldBe "description"
    }

    private fun buildChannel(): Channel {
        return Channel(
            "title",
            "link",
            "description",
            Image(
                "title",
                "url",
                "link",
                "description"
            ),
            "lastBuildDate",
            "updatePeriod",
            listOf(
                Article(
                    "guid",
                    "title",
                    "author",
                    "link",
                    PUB_DATE,
                    "description",
                    "content",
                    "image",
                    "audio",
                    "video",
                    "sourceName",
                    "sourceUrl",
                    listOf("categories"),
                    ItunesArticleData(
                        "author",
                        AUDIO_LENGTH_IN_SECONDS.toString(),
                        "episode",
                        "episodeType",
                        "explicit",
                        "image",
                        listOf("keywords"),
                        "subtitle",
                        "summary"
                    )
                )
            ),
            ItunesChannelData(
                "author",
                listOf("categories"),
                "2000",
                "explicit",
                "image",
                listOf("keywords"),
                "newsFeedUrl",
                ItunesOwner(
                    "name",
                    "email"
                ),
                "subtitle",
                "summary",
                "type"
            )
        )
    }
}

private const val AUDIO_LENGTH_IN_SECONDS = 896
private const val PUB_DATE = "Mon, 27 Jun 2022 04:00:33 GMT"
