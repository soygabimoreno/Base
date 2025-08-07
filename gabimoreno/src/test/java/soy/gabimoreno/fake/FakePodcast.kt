package soy.gabimoreno.fake

import soy.gabimoreno.data.local.podcast.model.PodcastDbModel
import soy.gabimoreno.domain.model.audio.Saga

fun buildPodcasts() =
    listOf<PodcastDbModel>(
        buildPodcast("1"),
        buildPodcast("2"),
        buildPodcast("3"),
    )

fun buildPodcast(
    id: String,
    hasBeenListened: Boolean = false,
    markedAsFavorite: Boolean = false,
) = PodcastDbModel(
    id = id,
    url = "",
    audioUrl = "",
    imageUrl = "",
    saga = Saga(author = "This is publisher", title = "This is saga title"),
    thumbnailUrl = "",
    pubDateMillis = 0,
    title = "This is a title",
    audioLengthInSeconds = 2700,
    description = "This is a description",
    hasBeenListened = hasBeenListened,
    markedAsFavorite = markedAsFavorite,
)
