package soy.gabimoreno.fake

import soy.gabimoreno.data.local.premiumaudio.PremiumAudioDbModel
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.audio.Saga

fun buildPremiumAudioDbModels() =
    listOf(
        buildPremiumAudioDbModel().copy(id = "1"),
        buildPremiumAudioDbModel().copy(id = "2"),
        buildPremiumAudioDbModel().copy(id = "3"),
    )

fun buildPremiumAudioDbModel(
    id: String = "1",
    hasBeenListened: Boolean = false,
    markedAsFavorite: Boolean = false,
): PremiumAudioDbModel =
    PremiumAudioDbModel(
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
        category = Category.PREMIUM,
        excerpt = "excerpt",
        hasBeenListened = hasBeenListened,
        markedAsFavorite = markedAsFavorite,
    )
