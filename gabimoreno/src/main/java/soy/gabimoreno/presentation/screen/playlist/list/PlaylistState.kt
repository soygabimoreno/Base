package soy.gabimoreno.presentation.screen.playlist.list

import soy.gabimoreno.domain.model.content.Playlist

data class PlaylistState(
    val isLoading: Boolean = true,
    val shouldIShowAddPlaylistDialog: Boolean = false,
    val playlists: List<Playlist> = emptyList(),
    val dialogTitle: String = emptyString,
    val dialogDescription: String = emptyString,
    val dialogTitleError: Boolean = false,
    val dialogDescriptionError: Boolean = false,
)

internal const val emptyString = ""
