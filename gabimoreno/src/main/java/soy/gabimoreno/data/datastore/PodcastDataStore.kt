package soy.gabimoreno.data.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import soy.gabimoreno.domain.model.PodcastSearch

class PodcastDataStore(
    private val context: Context
) {
    private val lastAPIFetchMillis = longPreferencesKey("last_api_fetch_millis")
    private val podcastSearchResult = stringPreferencesKey("podcast_search_result")

    companion object {
        private const val TAG = "PodcastDataStore"
    }

    suspend fun storePodcastSearchResult(data: PodcastSearch) {
        context.podcastDataStore.edit { preferences ->
            val jsonString = Gson().toJson(data)
            Log.i(TAG, jsonString)
            preferences[lastAPIFetchMillis] = System.currentTimeMillis()
            preferences[podcastSearchResult] = jsonString
        }
    }

    suspend fun readLastPodcastSearchResult(): PodcastSearch {
        return context.podcastDataStore.data.map { preferences ->
            val jsonString = preferences[podcastSearchResult]
            Gson().fromJson(jsonString, PodcastSearch::class.java)
        }.first()
    }

    suspend fun canFetchAPI(): Boolean {
        return context.podcastDataStore.data.map { preferences ->
            val epochMillis = preferences[lastAPIFetchMillis]

            return@map if (epochMillis != null) {
                val minDiffMillis = 36 * 60 * 60 * 1000L
                val now = System.currentTimeMillis()
                (now - minDiffMillis) > epochMillis
            } else {
                true
            }
        }.first()
    }
}

private val Context.podcastDataStore: DataStore<Preferences> by preferencesDataStore(name = "podcasts")
