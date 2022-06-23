package soy.gabimoreno.data.network.mapper

import com.prof.rssparser.Article
import com.prof.rssparser.Channel
import soy.gabimoreno.core.findUrl
import soy.gabimoreno.domain.model.Episode
import soy.gabimoreno.domain.model.Podcast
import soy.gabimoreno.domain.model.PodcastSearch
import java.util.*

fun Channel.toDomain(): PodcastSearch {
    val numberOfEpisodes = articles.size.toLong()
    return PodcastSearch(
        count = numberOfEpisodes,
        total = numberOfEpisodes,
        results = articles.toDomain()
    )
}

fun List<Article>.toDomain(): List<Episode> {
    return map { it.toDomain() }
}

fun Article.toDomain(): Episode {
    return run {
        val description = description?.removeAnchorMessage() ?: ""
        Episode(
            id = guid!!.replace("https://www.ivoox.com/", ""),
//            url = description.getEpisodeUrl(), // TODO: OutOfMemoryError
            url = "https://gabimoreno.soy",
            audioUrl = audio!!,
            imageUrl = itunesArticleData?.image ?: "",
            podcast = Podcast(
                id = "1234",  // TODO: Will be required ???
                image = "", // TODO: Fill these properties
                thumbnail = "",
                titleOriginal = "",
                listennotesURL = "",
                publisherOriginal = ""
            ),
            thumbnailUrl = itunesArticleData?.image ?: "",
            pubDateMillis = Date(pubDate).time,
            title = title ?: "",
            audioLengthSeconds = itunesArticleData?.duration?.toInt() ?: 0,
            description = description
        )
    }
}

private fun String.removeAnchorMessage() = replace(ANCHOR_MESSAGE, "")

private fun String.getEpisodeUrl() = findUrl()

internal const val ANCHOR_MESSAGE =
    "\n\n" +
        "--- \n" +
        "\n" +
        "Send in a voice message: https://anchor.fm/losandroides/message"
