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
        navCommand = NavCommand.ContentType(Feature.SENIOR_AUDIOS),
        icon = Icons.Default.Mic,
        titleResId = R.string.nav_item_senior_audios,
    ),
    PODCAST(
        navCommand = NavCommand.ContentType(Feature.PODCAST),
        icon = Icons.Default.Podcasts,
        titleResId = R.string.nav_item_podcast,
    ),
    PREMIUM(
        navCommand = NavCommand.ContentType(Feature.PREMIUM_AUDIOS),
        icon = Icons.Default.WorkspacePremium,
        titleResId = R.string.nav_item_premium_audios,
    ),
    COURSES(
        navCommand = NavCommand.ContentType(Feature.AUDIO_COURSES),
        icon = Icons.Default.Diamond,
        titleResId = R.string.nav_item_audio_courses,
    ),
    PROFILE(
        navCommand = NavCommand.ContentType(Feature.PROFILE),
        icon = Icons.Default.Person,
        titleResId = R.string.nav_item_profile,
    ),
}
