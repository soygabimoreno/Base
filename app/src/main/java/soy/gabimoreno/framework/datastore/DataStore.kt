package soy.gabimoreno.framework.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private const val DATA_STORE = "DATA_STORE"
internal val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE)
