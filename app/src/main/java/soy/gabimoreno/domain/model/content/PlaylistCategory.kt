package soy.gabimoreno.domain.model.content

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.School
import androidx.compose.ui.graphics.vector.ImageVector
import soy.gabimoreno.presentation.theme.Orange
import soy.gabimoreno.presentation.theme.PurpleDark

enum class PlaylistCategory(
    val id: Int,
    val color: androidx.compose.ui.graphics.Color,
    val icon: ImageVector,
) {
    USER_PLAYLIST(
        id = 1,
        color = Orange,
        icon = Icons.Default.LibraryMusic,
    ),
    ROADMAP_PLAYLIST(
        id = 2,
        color = PurpleDark,
        icon = Icons.Default.School,
    ),
}

fun getPlaylistCategoryFromId(id: Int) = PlaylistCategory.entries.first { it.id == id }
