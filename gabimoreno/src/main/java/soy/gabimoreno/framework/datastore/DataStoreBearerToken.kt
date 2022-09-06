package soy.gabimoreno.framework.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val BEARER_TOKEN = "BEARER_TOKEN"
private val key = stringPreferencesKey(BEARER_TOKEN)

fun Context.getBearerToken(): Flow<String> {
    return dataStore.data
        .map { preferences ->
            preferences[key] ?: EMPTY_BEARER_TOKEN
        }
}

suspend fun Context.setBearerToken(bearerToken: String) {
    dataStore.edit { settings ->
        settings[key] = bearerToken
    }
}

internal const val EMPTY_BEARER_TOKEN = ""
