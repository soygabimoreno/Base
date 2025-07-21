package soy.gabimoreno.framework.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun Context.getPassword(): Flow<String> =
    dataStore.data
        .map { preferences ->
            preferences[key] ?: EMPTY_PASSWORD
        }

suspend fun Context.setPassword(password: String) {
    dataStore.edit { settings ->
        settings[key] = password
    }
}

private const val EMPTY_PASSWORD = ""
private const val PASSWORD = "PASSWORD"
private val key = stringPreferencesKey(PASSWORD)
