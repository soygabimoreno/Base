package soy.gabimoreno.framework.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun Context.dataStoreShouldIReversePodcastOrder(): Flow<Boolean> =
    dataStore.data
        .map { preferences ->
            preferences[key] ?: DEFAULT_FLAG_REVERSE_PODCAST_ORDER
        }

suspend fun Context.setDataStoreShouldIReversePodcastOrder(shouldReverse: Boolean) {
    dataStore.edit { settings ->
        settings[key] = shouldReverse
    }
}

private const val DEFAULT_FLAG_REVERSE_PODCAST_ORDER = false
private const val SHOULD_I_REVERSE_PODCAST_ORDER = "SHOULD_I_REVERSE_PODCAST_ORDER"
private val key = booleanPreferencesKey(SHOULD_I_REVERSE_PODCAST_ORDER)
