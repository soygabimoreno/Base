package soy.gabimoreno.framework.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun Context.dataStoreGetLastAudioListenedId(): Flow<String> =
    dataStore.data
        .map { preferences ->
            preferences[key] ?: DEFAULT_LAST_AUDIO_LISTENED_ID
        }

suspend fun Context.setDataStoreLastAudioListenedId(audioId: String) {
    dataStore.edit { settings ->
        settings[key] = audioId
    }
}

private const val DEFAULT_LAST_AUDIO_LISTENED_ID = "0"
private const val LAST_AUDIO_LISTENED_ID = "LAST_AUDIO_LISTENED_ID"
private val key = stringPreferencesKey(LAST_AUDIO_LISTENED_ID)
