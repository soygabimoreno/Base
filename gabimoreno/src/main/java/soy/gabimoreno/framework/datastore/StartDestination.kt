package soy.gabimoreno.framework.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import soy.gabimoreno.presentation.navigation.Feature

private const val START_DESTINATION = "START_DESTINATION"
private val key = stringPreferencesKey(START_DESTINATION)

fun Context.getStartDestination(): Flow<String> {
    return dataStore.data
        .map { preferences ->
            preferences[key] ?: Feature.PODCAST.route
        }
}

suspend fun Context.setStartDestination(feature: Feature) {
    dataStore.edit { settings ->
        settings[key] = feature.route
    }
}
