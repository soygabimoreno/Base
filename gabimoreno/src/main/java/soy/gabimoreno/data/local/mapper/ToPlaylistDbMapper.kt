package soy.gabimoreno.data.local.mapper

import soy.gabimoreno.data.local.playlist.model.PlaylistDbModel
import soy.gabimoreno.data.local.playlist.model.PlaylistItemsDbModel
import soy.gabimoreno.data.local.premiumaudio.PremiumAudioDbModel
import soy.gabimoreno.domain.model.content.Playlist
import soy.gabimoreno.domain.model.content.PlaylistAudioItem

fun Playlist.toPlaylistDbModel() =
    PlaylistDbModel(
        id = id,
        categoryId = category.id,
        description = description,
        position = position,
        title = title,
    )

fun PlaylistAudioItem.toPlaylistItemDbModel(playlistId: Int) =
    PlaylistItemsDbModel(
        id = playlistItemId,
        audioItemId = id,
        playlistId = playlistId,
        position = position,
    )

fun PlaylistAudioItem.toPremiumAudioDbModel() =
    PremiumAudioDbModel(
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
        category = category,
        excerpt = excerpt,
        hasBeenListened = hasBeenListened,
        markedAsFavorite = markedAsFavorite,
    )
