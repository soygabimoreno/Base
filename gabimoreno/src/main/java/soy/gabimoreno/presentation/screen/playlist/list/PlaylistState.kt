package soy.gabimoreno.presentation.screen.playlist.list

import soy.gabimoreno.domain.model.content.Playlist

data class PlaylistState(
    val isLoading: Boolean = true,
    val shouldIShowAddPlaylistDialog: Boolean = false,
    val shouldIShowConfirmDialog: Boolean = false,
    val playlists: List<Playlist> = emptyList(),
    val selectedPlaylistId: Int? = null,
    val dialogTitle: String = EMPTY_STRING,
    val dialogDescription: String = EMPTY_STRING,
    val dialogTitleError: Boolean = false,
    val dialogDescriptionError: Boolean = false,
)

internal const val EMPTY_STRING = ""
