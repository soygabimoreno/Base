package soy.gabimoreno.data.cloud.audiosync.response

interface SyncableAudio {
    val id: String
    val hasBeenListened: Boolean
    val markedAsFavorite: Boolean
}
