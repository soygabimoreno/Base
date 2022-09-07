package soy.gabimoreno.data.network.mapper

import com.prof.rssparser.Article
import com.prof.rssparser.Channel
import com.prof.rssparser.ItunesArticleData
import soy.gabimoreno.domain.model.GABI_MORENO_WEB_BASE_URL
import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.domain.model.podcast.Podcast
import soy.gabimoreno.domain.model.podcast.PodcastSearch
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
    podcastTitle: String,
): List<Episode> {
    return map { it.toDomain(podcastAuthor, podcastTitle) }
}

fun Article.toDomain(
    podcastAuthor: String,
    podcastTitle: String,
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
        val dateFormat = SimpleDateFormat(HOURS_MINUTES_SECONDS_PATTERN, Locale.ROOT)
        val date = dateFormat.parse(duration)
        val calendar = Calendar.getInstance()
        if (date == null) return EPISODE_AUDIO_LENGTH_DEFAULT_DURATION
        calendar.time = date
        val hours = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)
        val seconds = calendar.get(Calendar.SECOND)
        return hours * ONE_HOUR_IN_SECONDS + minutes * ONE_MINUTE_IN_SECONDS + seconds
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

private const val HOURS_MINUTES_SECONDS_PATTERN = "HH:mm:ss"
private const val ONE_MINUTE_IN_SECONDS = 60
private const val ONE_HOUR_IN_SECONDS = 60 * ONE_MINUTE_IN_SECONDS
