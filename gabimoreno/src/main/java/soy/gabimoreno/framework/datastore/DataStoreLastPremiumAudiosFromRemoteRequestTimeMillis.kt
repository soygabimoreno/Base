package soy.gabimoreno.framework.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val LAST_PREMIUM_AUDIOS_FROM_REMOTE_REQUEST_TIME_MILLIS =
    "LAST_PREMIUM_AUDIOS_FROM_REMOTE_REQUEST_TIME_MILLIS"
private val key = longPreferencesKey(LAST_PREMIUM_AUDIOS_FROM_REMOTE_REQUEST_TIME_MILLIS)

fun Context.getLastPremiumAudiosFromRemoteRequestTimeMillis(): Flow<Long> {
    return dataStore.data
        .map { preferences ->
            preferences[key] ?: EMPTY_LAST_PREMIUM_AUDIOS_FROM_REMOTE_REQUEST_TIME_MILLIS
        }
}

suspend fun Context.setLastPremiumAudiosFromRemoteRequestTimeMillis(timeMillis: Long) {
    dataStore.edit { settings ->
        settings[key] = timeMillis
    }
}

private const val EMPTY_LAST_PREMIUM_AUDIOS_FROM_REMOTE_REQUEST_TIME_MILLIS = -1L
