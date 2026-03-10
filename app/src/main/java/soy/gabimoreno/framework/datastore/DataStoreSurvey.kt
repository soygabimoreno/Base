package soy.gabimoreno.framework.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun Context.dataStoreGetLastSurveyId(): Flow<Int> =
    dataStore.data
        .map { preferences ->
            preferences[key] ?: DEFAULT_FLAG_SURVEY_ID
        }

suspend fun Context.setDataStoreLastSurveyId(surveyId: Int) {
    dataStore.edit { settings ->
        settings[key] = surveyId
    }
}

private const val DEFAULT_FLAG_SURVEY_ID = 0
private const val LAST_SURVEY_ID = "LAST_SURVEY_ID"
private val key = intPreferencesKey(LAST_SURVEY_ID)
