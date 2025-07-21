package soy.gabimoreno.data.remote.mapper

import androidx.annotation.VisibleForTesting
import com.prof.rssparser.Article
import com.prof.rssparser.Channel
import com.prof.rssparser.ItunesArticleData
import soy.gabimoreno.domain.model.GABI_MORENO_WEB_BASE_URL
import soy.gabimoreno.domain.model.audio.Saga
import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.domain.model.podcast.EpisodesWrapper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Channel.toDomain(): EpisodesWrapper {
    val numberOfEpisodes = articles.size.toLong()
    val title = title ?: LOS_ANDROIDES
    val podcastAuthor = title.uppercase()
    val podcastTitle = title
    return EpisodesWrapper(
        count = numberOfEpisodes,
        total = numberOfEpisodes,
        episodes = articles.toDomain(podcastAuthor, podcastTitle),
    )
}

fun List<Article>.toDomain(
    podcastAuthor: String,
    podcastTitle: String,
): List<Episode> = map { it.toDomain(podcastAuthor, podcastTitle) }

fun Article.toDomain(
    podcastAuthor: String,
    podcastTitle: String,
): Episode =
    run {
        val description = description?.removeAnchorMessage() ?: ""
        val imageUrl = getEpisodeCoverUrl()
        Episode(
            id = guid!!.replace(IVOOX_URL, ""),
            url = getEpisodeUrl(),
            audioUrl = audio!!,
            imageUrl = imageUrl,
            saga =
                Saga(
                    author = podcastAuthor,
                    title = podcastTitle,
                ),
            thumbnailUrl = imageUrl,
            pubDateMillis = Date(pubDate).time,
            title = title ?: "",
            audioLengthInSeconds = itunesArticleData.getAudioLengthInSeconds(),
            description = description,
            hasBeenListened = false,
            markedAsFavorite = false,
        )
    }

private fun String.removeAnchorMessage() = replace(ANCHOR_MESSAGE, "")

private fun Article.getEpisodeUrl(): String = "$GABI_MORENO_WEB_BASE_URL/${getEpisodeNumber()}"

private fun Article.getEpisodeCoverUrl(): String = itunesArticleData?.image.orEmpty()

private fun Article.getEpisodeNumber(): String {
    val episodeNumber =
        if (itunesArticleData != null && itunesArticleData?.episode != null) {
            itunesArticleData?.episode
        } else {
            title?.let { title ->
                val index = title.indexOfFirst { it == '.' }
                if (index == -1) return INVALID_EPISODE_NUMBER
                title.substring(0, index)
            } ?: ""
        }
    return episodeNumber.toString()
}

private fun ItunesArticleData?.getAudioLengthInSeconds(): Int {
    if (this == null) return EPISODE_AUDIO_LENGTH_DEFAULT_DURATION
    val duration = this.duration
    val durationInteger = duration?.toIntOrNull()
    if (durationInteger != null) {
        return durationInteger
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

private const val HOURS_MINUTES_SECONDS_PATTERN = "HH:mm:ss"
private const val ONE_MINUTE_IN_SECONDS = 60
private const val ONE_HOUR_IN_SECONDS = 60 * ONE_MINUTE_IN_SECONDS

@VisibleForTesting
internal const val PODCAST_COVER_SUFFIX = ".png"

@VisibleForTesting
internal const val INVALID_EPISODE_NUMBER = "-1"
