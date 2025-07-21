package soy.gabimoreno.data.cloud.audiosync.mappers

import soy.gabimoreno.data.cloud.playlist.response.CloudPlaylistResponse
import soy.gabimoreno.data.local.playlist.model.PlaylistDbModel
import soy.gabimoreno.domain.model.content.Playlist
import soy.gabimoreno.domain.model.content.PlaylistCategory

fun CloudPlaylistResponse.toPlaylistDbModel() =
    PlaylistDbModel(
        id = playlistId.toInt(),
        categoryId = categoryId,
        description = description,
        position = position,
        title = title,
    )

fun Playlist.toCloudPlaylistResponse() =
    CloudPlaylistResponse(
        playlistId = id.toString(),
        categoryId = category.id,
        title = title,
        description = description,
        position = position,
    )

fun CloudPlaylistResponse.toPlaylist() =
    Playlist(
        id = playlistId.toInt(),
        category =
            if (categoryId ==
                1
            ) {
                PlaylistCategory.USER_PLAYLIST
            } else {
                PlaylistCategory.ROADMAP_PLAYLIST
            },
        title = title,
        description = description,
        position = position,
        items = emptyList(),
    )
