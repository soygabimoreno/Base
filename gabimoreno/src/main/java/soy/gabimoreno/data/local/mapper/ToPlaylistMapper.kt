package soy.gabimoreno.data.local.mapper

import soy.gabimoreno.data.local.playlist.model.PlaylistDbModel
import soy.gabimoreno.domain.model.content.Playlist
import soy.gabimoreno.domain.model.content.PlaylistAudioItem
import soy.gabimoreno.domain.model.content.getPlaylistCategoryFromId

fun PlaylistDbModel.toPlaylistMapper(playlistWithItems: List<PlaylistAudioItem>) =
    Playlist(
        id = requireNotNull(id) { "PlaylistDbModel.id was null when mapping to domain" },
        category = getPlaylistCategoryFromId(categoryId),
        description = description,
        position = position,
        title = title,
        items = playlistWithItems,
    )
