package soy.gabimoreno.framework.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun Context.isMemberActive(): Flow<Boolean> {
    return dataStore.data
        .map { preferences ->
            preferences[key] ?: DEFAULT_IS_MEMBER_ACTIVE
        }
}

suspend fun Context.setMemberActive(isMemberActive: Boolean) {
    dataStore.edit { settings ->
        settings[key] = isMemberActive
    }
}

private const val DEFAULT_IS_MEMBER_ACTIVE = false
private const val IS_MEMBER_ACTIVE = "IS_MEMBER_ACTIVE"
private val key = booleanPreferencesKey(IS_MEMBER_ACTIVE)

