package soy.gabimoreno.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun AppBottomNavigation(
    currentRoute: String,
    onNavItemClick: (item: NavItem) -> Unit,
) {
    BottomNavigation {
        NavItem.entries.forEach { item ->
            val title = stringResource(id = item.titleResId)
            BottomNavigationItem(
                modifier = Modifier.padding(bottom = 8.dp),
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
