package soy.gabimoreno.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Podcasts
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.ui.graphics.vector.ImageVector
import soy.gabimoreno.R

enum class NavItem(
    val navCommand: NavCommand,
    val icon: ImageVector,
    @param:StringRes val titleResId: Int,
) {
    SENIOR(
        navCommand = NavCommand.ContentType(Feature.SENIOR),
        icon = Icons.Default.Mic,
        titleResId = R.string.nav_item_senior,
    ),
    PODCAST(
        navCommand = NavCommand.ContentType(Feature.PODCAST),
        icon = Icons.Default.Podcasts,
        titleResId = R.string.nav_item_podcast,
    ),
    PREMIUM(
        navCommand = NavCommand.ContentType(Feature.PREMIUM),
        icon = Icons.Default.WorkspacePremium,
        titleResId = R.string.nav_item_premium,
    ),
    COURSES(
        navCommand = NavCommand.ContentType(Feature.AUDIO_COURSES),
        icon = Icons.Default.Diamond,
        titleResId = R.string.nav_item_courses,
    ),
    PROFILE(
        navCommand = NavCommand.ContentType(Feature.PROFILE),
        icon = Icons.Default.Person,
        titleResId = R.string.nav_item_profile,
    ),
}
