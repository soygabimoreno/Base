package soy.gabimoreno.data.local.mapper

import soy.gabimoreno.data.local.podcast.model.PodcastDbModel
import soy.gabimoreno.data.local.podcast.model.SeniorDbModel
import soy.gabimoreno.data.remote.model.Category
import soy.gabimoreno.domain.model.content.PlaylistAudioItem
import soy.gabimoreno.domain.model.podcast.Episode

fun Episode.toPodcastDbModel() =
    PodcastDbModel(
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

fun Episode.toSeniorDbModel() =
    SeniorDbModel(
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

fun PodcastDbModel.toPlaylistAudioItem(position: Int) =
    PlaylistAudioItem(
        id = id,
        title = title,
        description = Category.PODCAST.title,
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
        category = Category.PODCAST,
    )

fun SeniorDbModel.toPlaylistAudioItem(position: Int) =
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
