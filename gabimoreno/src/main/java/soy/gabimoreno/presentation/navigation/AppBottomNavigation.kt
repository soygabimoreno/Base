package soy.gabimoreno.presentation.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun AppBottomNavigation(
    currentRoute: String,
    onNavItemClick: (item: NavItem) -> Unit,
) {
    BottomNavigation {
        NavItem.entries.forEach { item ->
            val title = stringResource(id = item.titleResId)
            BottomNavigationItem(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)),
                selected = currentRoute.contains(item.navCommand.feature.route),
                onClick = { onNavItemClick(item) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = title
                    )
                },
                label = {
                    Text(text = title)
                }
            )
        }
    }
}
