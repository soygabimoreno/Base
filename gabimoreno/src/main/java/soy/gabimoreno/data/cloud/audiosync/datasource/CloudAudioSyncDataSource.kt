package soy.gabimoreno.data.cloud.audiosync.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await
import soy.gabimoreno.data.cloud.audiosync.response.SyncableAudio
import javax.inject.Inject

class CloudDataSource<T : SyncableAudio> @Inject constructor(
    val firestore: FirebaseFirestore,
) {
    suspend inline fun <reified T : SyncableAudio> getAudioItems(
        email: String,
        collectionPath: String
    ): List<T> {
        return firestore.collection(USERS_PATH)
            .document(email)
            .collection(collectionPath)
            .get()
            .await()
            .toObjects<T>()
    }

    fun upsertAudioItemFields(
        email: String,
        collectionPath: String,
        itemId: String,
        updates: Map<String, Any>
    ) {
        firestore.collection(USERS_PATH)
            .document(email)
            .collection(collectionPath)
            .document(itemId)
            .set(
                updates,
                SetOptions.merge()
            )
    }

    suspend inline fun <reified T : SyncableAudio> batchUpdateFieldsForAllItems(
        email: String,
        collectionPath: String,
        updates: Map<String, Any>
    ) {
        val documentsSnapshot = firestore.collection(USERS_PATH)
            .document(email)
            .collection(collectionPath)
            .get()
            .await()

        val batch = firestore.batch()

        documentsSnapshot.documents.forEach { document ->
            batch.update(document.reference, updates)
        }

        batch.commit()
    }

    fun deleteAudioItem(email: String, collectionPath: String, itemId: String) {
        firestore.collection(USERS_PATH)
            .document(email)
            .collection(collectionPath)
            .document(itemId)
            .delete()
    }

    suspend inline fun <reified T : SyncableAudio> deleteAllAudioItems(
        email: String,
        collectionPath: String
    ) {
        val items = getAudioItems<T>(email, collectionPath)
        items.forEach { item ->
            deleteAudioItem(email, collectionPath, item.id)
        }
    }
}

const val AUDIOCOURSES_PATH = "audiocourses"
const val PREMIUM_AUDIOS_PATH = "premium_audios"
const val USERS_PATH = "base_users"
