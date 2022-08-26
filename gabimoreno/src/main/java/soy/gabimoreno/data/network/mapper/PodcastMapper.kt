package soy.gabimoreno.data.network.mapper

import com.prof.rssparser.Article
import com.prof.rssparser.Channel
import com.prof.rssparser.ItunesArticleData
import soy.gabimoreno.domain.model.Episode
import soy.gabimoreno.domain.model.GABI_MORENO_WEB_BASE_URL
import soy.gabimoreno.domain.model.Podcast
import soy.gabimoreno.domain.model.PodcastSearch
import java.text.SimpleDateFormat
import java.util.*

fun Channel.toDomain(): PodcastSearch {
    val numberOfEpisodes = articles.size.toLong()
    val title = title ?: LOS_ANDROIDES
    val podcastAuthor = title.uppercase()
    val podcastTitle = title
    return PodcastSearch(
        count = numberOfEpisodes,
        total = numberOfEpisodes,
        results = articles.toDomain(podcastAuthor, podcastTitle)
    )
}

fun List<Article>.toDomain(
    podcastAuthor: String,
    podcastTitle: String
): List<Episode> {
    return map { it.toDomain(podcastAuthor, podcastTitle) }
}

fun Article.toDomain(
    podcastAuthor: String,
    podcastTitle: String
): Episode {
    return run {
        val description = description?.removeAnchorMessage() ?: ""
        val imageUrl = itunesArticleData?.image ?: EPISODE_EMPTY_IMAGE_URL
        Episode(
            id = guid!!.replace(IVOOX_URL, ""),
            url = getEpisodeUrl(),
            audioUrl = audio!!,
            imageUrl = imageUrl,
            podcast = Podcast(
                author = podcastAuthor,
                title = podcastTitle
            ),
            thumbnailUrl = imageUrl,
            pubDateMillis = Date(pubDate).time,
            title = title ?: "",
            audioLengthInSeconds = itunesArticleData.getAudioLengthInSeconds(),
            description = description
        )
    }
}

private fun String.removeAnchorMessage() = replace(ANCHOR_MESSAGE, "")

private fun Article.getEpisodeUrl(): String {
    val episodeNumber = if (itunesArticleData != null && itunesArticleData?.episode != null) {
        itunesArticleData?.episode
    } else {
        title?.let { title ->
            val index = title.indexOfFirst { it == '.' }
            title.substring(0, index)
        } ?: ""
    }
    return "$GABI_MORENO_WEB_BASE_URL/$episodeNumber"
}

private fun ItunesArticleData?.getAudioLengthInSeconds(): Int {
    if (this == null) return EPISODE_AUDIO_LENGTH_DEFAULT_DURATION
    val duration = this.duration
    if (duration?.toIntOrNull() != null) {
        return duration.toInt()
    } else {
        if (duration == null) return EPISODE_AUDIO_LENGTH_DEFAULT_DURATION
        val dateFormat = SimpleDateFormat("HH:mm:ss")
        val date = dateFormat.parse(duration)
        return date.hours * 60 * 60 + date.minutes * 60 + date.seconds // TODO: Put this cleaner
    }
}

internal const val LOS_ANDROIDES = "Los androides"
internal const val ANCHOR_MESSAGE =
    "\n\n" +
        "--- \n" +
        "\n" +
        "Send in a voice message: https://anchor.fm/losandroides/message"
internal const val IVOOX_URL = "https://www.ivoox.com/"
internal const val EPISODE_AUDIO_LENGTH_DEFAULT_DURATION = 0
internal const val EPISODE_EMPTY_IMAGE_URL = ""
