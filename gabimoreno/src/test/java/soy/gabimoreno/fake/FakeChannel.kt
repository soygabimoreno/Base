package soy.gabimoreno.fake

import com.prof.rssparser.*
import soy.gabimoreno.data.remote.mapper.ANCHOR_MESSAGE
import soy.gabimoreno.data.remote.mapper.IVOOX_URL
import soy.gabimoreno.data.remote.mapper.PODCAST_COVER_PREFIX
import soy.gabimoreno.data.remote.mapper.PODCAST_COVER_SUFFIX
import soy.gabimoreno.domain.model.GABI_MORENO_WEB_BASE_URL

fun buildChannel(url: String): Channel {
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

private const val EPISODE_ID = "guid"
private const val EPISODE_NUMBER = "1234"
private const val EPISODE_AUDIO_URL = "audio"
private const val EPISODE_IMAGE_URL =
    "$GABI_MORENO_WEB_BASE_URL/$PODCAST_COVER_PREFIX$EPISODE_NUMBER$PODCAST_COVER_SUFFIX"
private const val EPISODE_TITLE = "$EPISODE_NUMBER. Title"
private const val EPISODE_AUDIO_LENGTH_IN_SECONDS = 896
private const val EPISODE_AUDIO_LENGTH_IN_TIME_FORMATTED = "01:51:15"
private val EPISODE_AUDIO_LENGTH_NULL = null
private const val EPISODE_PUB_DATE = "Mon, 27 Jun 2022 04:00:33 GMT"
private const val EPISODE_DESCRIPTION = "description"

private const val PODCAST_TITLE = "podcastTitle"
