package soy.gabimoreno.fake

import soy.gabimoreno.domain.model.Episode
import soy.gabimoreno.domain.model.Podcast

fun buildEpisode() = Episode(
    id = "1",
    url = "",
    audioUrl = "",
    imageUrl = "",
    podcast = Podcast("This is publisher", "This is podcast title"),
    thumbnailUrl = "",
    pubDateMillis = 0,
    title = "This is a title",
    audioLengthSeconds = 2700,
    description = "This is a description"
)
