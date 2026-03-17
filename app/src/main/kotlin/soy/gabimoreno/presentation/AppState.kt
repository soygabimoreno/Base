package soy.gabimoreno.presentation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import soy.gabimoreno.framework.datastore.getStartDestination
import soy.gabimoreno.framework.datastore.setStartDestination
import soy.gabimoreno.presentation.navigation.Feature

@Composable
fun rememberAppState(): AppState {
    val context = LocalContext.current
    return remember {
        AppState(context)
    }
}

class AppState(
    private val context: Context,
) {
    val startDestination: String =
        runBlocking {
            // TODO: This is blocking the UI. Do it better
            context.getStartDestination().first()
        }

    fun setStartDestination(feature: Feature) {
        runBlocking {
            // TODO: This is blocking the UI. Do it better
            context.setStartDestination(feature)
        }
    }
}
