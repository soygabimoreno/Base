package soy.gabimoreno.data.local.mapper

import soy.gabimoreno.data.local.premiumaudio.PremiumAudioDbModel
import soy.gabimoreno.domain.model.content.PlaylistAudioItem
import soy.gabimoreno.domain.model.content.PremiumAudio

fun PremiumAudioDbModel.toPremiumAudio() =
    PremiumAudio(
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
        category = category,
        excerpt = excerpt,
        markedAsFavorite = markedAsFavorite,
    )

fun PremiumAudioDbModel.toPlaylistAudioItem(position: Int) =
    PlaylistAudioItem(
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
        category = category,
        excerpt = excerpt,
        position = position,
        playlistItemId = null,
        markedAsFavorite = markedAsFavorite,
    )
