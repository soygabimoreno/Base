package soy.gabimoreno.framework.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStoreInAppReviewCounter: Flow<Int>
    get() = dataStore.data
        .map { it[key] ?: DEFAULT_SHOW_IN_APP_REVIEW }
        .distinctUntilChanged()

suspend fun Context.dataStoreShouldIShowInAppReview(): Boolean {
    val counterAudios = dataStore.data
        .map { preferences ->
            preferences[key] ?: DEFAULT_SHOW_IN_APP_REVIEW
        }.first()

    return when (counterAudios) {
        InAppReviewPrefs.USER_DO_NOT_SHOW_AGAIN -> false
        REPEAT_SHOW_IN_APP_REVIEW -> {
            setDataStoreShouldIShowInAppReview(EMPTY_SHOW_IN_APP_REVIEW)
            true
        }

        else -> {
            setDataStoreShouldIShowInAppReview(counterAudios + 1)
            true
        }
    }
}

suspend fun Context.setDataStoreShouldIShowInAppReview(counterAudios: Int) {
    dataStore.edit { settings ->
        settings[key] = counterAudios
    }
}

private const val SHOULD_I_SHOW_IN_APP_REVIEW = "SHOULD_I_SHOW_IN_APP_REVIEW"
private val key = intPreferencesKey(SHOULD_I_SHOW_IN_APP_REVIEW)
private const val DEFAULT_SHOW_IN_APP_REVIEW = 2
private const val EMPTY_SHOW_IN_APP_REVIEW = 0
private const val REPEAT_SHOW_IN_APP_REVIEW = 3

object InAppReviewPrefs {
    const val USER_DO_NOT_SHOW_AGAIN = -1
}
