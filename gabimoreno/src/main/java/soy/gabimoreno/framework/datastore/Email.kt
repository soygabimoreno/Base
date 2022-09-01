package soy.gabimoreno.framework.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val EMAIL = "EMAIL"
private val key = stringPreferencesKey(EMAIL)

fun Context.getEmail(): Flow<String> {
    return dataStore.data
        .map { preferences ->
            preferences[key] ?: EMPTY_EMAIL
        }
}

suspend fun Context.setEmail(email: String) {
    dataStore.edit { settings ->
        settings[key] = email
    }
}

private const val EMPTY_EMAIL = ""
