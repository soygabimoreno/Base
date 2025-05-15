package soy.gabimoreno.framework.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun Context.dataStoreShouldIReloadAudiosCourses(): Flow<Boolean> {
    return dataStore.data
        .map { preferences ->
            preferences[key] ?: DEFAULT_FLAG_RELOAD_AUDIOCOURSES
        }
}

suspend fun Context.setDataStoreShouldIReloadAudiosCourses(shouldReload: Boolean) {
    dataStore.edit { settings ->
        settings[key] = shouldReload
    }
}

private const val DEFAULT_FLAG_RELOAD_AUDIOCOURSES = false
private const val SHOULD_I_RELOAD_AUDIOCOURSES = "SHOULD_I_RELOAD_AUDIOCOURSES"
private val key = booleanPreferencesKey(SHOULD_I_RELOAD_AUDIOCOURSES)
