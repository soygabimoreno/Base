package soy.gabimoreno.data.cloud.playlist.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await
import soy.gabimoreno.data.cloud.audiosync.datasource.USERS_PATH
import soy.gabimoreno.data.cloud.playlist.response.CloudPlaylistItemResponse
import soy.gabimoreno.data.cloud.playlist.response.CloudPlaylistResponse
import javax.inject.Inject

class CloudPlaylistDataSource
    @Inject
    constructor(
        private val firestore: FirebaseFirestore,
    ) {
        suspend fun getPlaylists(email: String): List<CloudPlaylistResponse> =
            firestore
                .collection(USERS_PATH)
                .document(email)
                .collection(PLAYLISTS_PATH)
                .get()
                .await()
                .toObjects<CloudPlaylistResponse>()

        fun updatePlaylist(
            email: String,
            playlist: CloudPlaylistResponse,
        ) {
            firestore
                .collection(USERS_PATH)
                .document(email)
                .collection(PLAYLISTS_PATH)
                .document(playlist.playlistId)
                .set(playlist)
        }

        suspend fun deletePlaylist(
            email: String,
            playlistId: String,
        ) {
            deleteAllPlaylistItems(email, playlistId)

            firestore
                .collection(USERS_PATH)
                .document(email)
                .collection(PLAYLISTS_PATH)
                .document(playlistId)
                .delete()
                .await()
        }

        suspend fun deleteAllPlaylists(email: String) {
            val playlists = getPlaylists(email)
            playlists.forEach { playlist ->
                deletePlaylist(email, playlist.playlistId)
            }
        }

        suspend fun getPlaylistItems(
            email: String,
            playlistId: String,
        ): List<CloudPlaylistItemResponse> =
            firestore
                .collection(USERS_PATH)
                .document(email)
                .collection(PLAYLISTS_PATH)
                .document(playlistId)
                .collection(PLAYLIST_ITEMS_PATH)
                .orderBy("position")
                .get()
                .await()
                .toObjects<CloudPlaylistItemResponse>()

        suspend fun savePlaylistItem(
            email: String,
            playlistItem: CloudPlaylistItemResponse,
        ) {
            firestore
                .collection(USERS_PATH)
                .document(email)
                .collection(PLAYLISTS_PATH)
                .document(playlistItem.playlistId)
                .collection(PLAYLIST_ITEMS_PATH)
                .document(playlistItem.id)
                .set(playlistItem)
                .await()
        }

        suspend fun deletePlaylistItem(
            email: String,
            playlistId: String,
            itemId: String,
        ) {
            firestore
                .collection(USERS_PATH)
                .document(email)
                .collection(PLAYLISTS_PATH)
                .document(playlistId)
                .collection(PLAYLIST_ITEMS_PATH)
                .document(itemId)
                .delete()
                .await()
        }

        private suspend fun deleteAllPlaylistItems(
            email: String,
            playlistId: String,
        ) {
            val items = getPlaylistItems(email, playlistId)
            items.forEach { item ->
                deletePlaylistItem(email, playlistId, item.id)
            }
        }
    }

private const val PLAYLISTS_PATH = "playlists"
private const val PLAYLIST_ITEMS_PATH = "items"
