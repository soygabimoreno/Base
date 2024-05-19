package soy.gabimoreno.fake

import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.audio.Saga
import soy.gabimoreno.domain.model.content.PremiumAudio

fun buildPremiumAudios() = listOf(
    buildPremiumAudio().copy(id = "1"),
    buildPremiumAudio().copy(id = "2"),
    buildPremiumAudio().copy(id = "3")
)

fun buildPremiumAudio(): PremiumAudio = PremiumAudio(
    id = "1",
    url = "",
    audioUrl = "",
    imageUrl = "",
    saga = Saga(author = "This is publisher", title = "This is saga title"),
    thumbnailUrl = "",
    pubDateMillis = 0,
    title = "This is a title",
    audioLengthInSeconds = 2700,
    description = "This is a description",
    category = Category.PREMIUM,
    excerpt = "excerpt"
)
