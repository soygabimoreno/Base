package soy.gabimoreno.data.local.mapper

import soy.gabimoreno.data.local.podcast.model.PodcastDbModel
import soy.gabimoreno.domain.model.podcast.Episode

fun PodcastDbModel.toEpisode() =
    Episode(
        id = id,
        title = title,
        description = description,
        saga = saga,
        url = url,
        audioUrl = audioUrl,
        imageUrl = imageUrl,
        thumbnailUrl = thumbnailUrl,
        pubDateMillis = pubDateMillis,
        audioLengthInSeconds = audioLengthInSeconds,
        hasBeenListened = hasBeenListened,
        markedAsFavorite = markedAsFavorite,
    )
