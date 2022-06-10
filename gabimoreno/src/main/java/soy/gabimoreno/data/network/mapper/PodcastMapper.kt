package soy.gabimoreno.data.network.mapper

import soy.gabimoreno.data.network.model.ItemApiModel
import soy.gabimoreno.data.network.model.RssApiModel
import soy.gabimoreno.domain.model.Episode
import soy.gabimoreno.domain.model.Podcast
import soy.gabimoreno.domain.model.PodcastSearch
import java.util.*

fun RssApiModel.toDomain(): PodcastSearch {
    val numberOfEpisodes = channel!!.items!!.size.toLong()
    return PodcastSearch(
        count = numberOfEpisodes,
        total = numberOfEpisodes,
        results = channel!!.items!!.toDomain()
    )
}

fun List<ItemApiModel>.toDomain(): List<Episode> {
    return map { it.toDomain() }
}

fun ItemApiModel.toDomain(): Episode {
    return Episode(
        id = "", // TODO
        link = link, // TODO: ???
        audio = enclosure!!.url,
        image = image!!.href,
        podcast = Podcast(
            id = "1234",  // TODO: Will be required ???
            image = "", // TODO: Fill these properties
            thumbnail = "",
            titleOriginal = "",
            listennotesURL = "",
            publisherOriginal = ""
        ),
        thumbnail = image!!.href,
        pubDateMS = Date(pubDate).time,
        titleOriginal = title,
        listennotesURL = link, // TODO: ???
        audioLengthSec = duration!!.text,
        explicitContent = explicit!!.text.toBoolean(),
        descriptionOriginal = description
    )
}
