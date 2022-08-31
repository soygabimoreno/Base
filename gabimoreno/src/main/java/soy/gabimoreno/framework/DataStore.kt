package soy.gabimoreno.framework

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import soy.gabimoreno.presentation.navigation.Feature

private const val DATA_STORE = "DATA_STORE"
internal val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE)

private const val START_DESTINATION = "START_DESTINATION"
private val startDestinationKey = stringPreferencesKey(START_DESTINATION)

fun Context.getStartDestination(): Flow<String> {
    return dataStore.data
        .map { preferences ->
            preferences[startDestinationKey] ?: Feature.PODCAST.route
        }
}

suspend fun Context.setStartDestination(feature: Feature) {
    dataStore.edit { settings ->
        settings[startDestinationKey] = feature.route
    }
}
