package soy.gabimoreno.fake

import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.domain.model.podcast.Podcast

fun buildEpisodes() = listOf(
    buildEpisode().copy(id = "1"),
    buildEpisode().copy(id = "2"),
    buildEpisode().copy(id = "3")
)

fun buildEpisode() = Episode(
    id = "1",
    url = "",
    audioUrl = "",
    imageUrl = "",
    podcast = Podcast("This is publisher", "This is podcast title"),
    thumbnailUrl = "",
    pubDateMillis = 0,
    title = "This is a title",
    audioLengthInSeconds = 2700,
    description = "This is a description"
)
