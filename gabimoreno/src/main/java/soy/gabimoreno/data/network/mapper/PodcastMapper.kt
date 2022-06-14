package soy.gabimoreno.data.network.mapper

import com.prof.rssparser.Article
import com.prof.rssparser.Channel
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
    return Episode(
        id = guid!!.replace("https://www.ivoox.com/", ""),
        link = link,
        audio = audio!!,
        image = itunesArticleData?.image ?: "",
        podcast = Podcast(
            id = "1234",  // TODO: Will be required ???
            image = "", // TODO: Fill these properties
            thumbnail = "",
            titleOriginal = "",
            listennotesURL = "",
            publisherOriginal = ""
        ),
        thumbnail = itunesArticleData?.image ?: "",
        pubDateMS = Date(pubDate).time,
        titleOriginal = title ?: "",
        listennotesURL = link ?: "",
        audioLengthSec = itunesArticleData?.duration?.toLong() ?: 0,
        explicitContent = itunesArticleData?.explicit.toBoolean(),
        descriptionOriginal = description ?: ""
    )
}
