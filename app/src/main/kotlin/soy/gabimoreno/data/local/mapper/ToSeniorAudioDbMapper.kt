package soy.gabimoreno.data.local.mapper

import soy.gabimoreno.data.local.podcast.model.SeniorAudioDbModel
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.content.PlaylistAudioItem
import soy.gabimoreno.domain.model.content.SeniorAudio

fun SeniorAudio.toSeniorDbModel() =
    SeniorAudioDbModel(
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

fun SeniorAudioDbModel.toPlaylistAudioItem(position: Int) =
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
        position = position,
        playlistItemId = null,
        markedAsFavorite = markedAsFavorite,
        excerpt = DEFAULT_EXCERPT,
        category = Category.PODCAST, // TODO: Should we have a different category for seniors?
    )

private const val DEFAULT_EXCERPT = ""
