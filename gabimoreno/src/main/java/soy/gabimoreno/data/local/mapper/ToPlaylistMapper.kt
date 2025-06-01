package soy.gabimoreno.data.local.mapper

import soy.gabimoreno.data.local.playlist.model.PlaylistDbModel
import soy.gabimoreno.domain.model.content.Playlist
import soy.gabimoreno.domain.model.content.PlaylistAudioItem

fun PlaylistDbModel.toPlaylistMapper(playlistWithItems: List<PlaylistAudioItem>) = Playlist(
    id = requireNotNull(id) { "PlaylistDbModel.id was null when mapping to domain" },
    title = title,
    description = description,
    position = position,
    items = playlistWithItems,
)
