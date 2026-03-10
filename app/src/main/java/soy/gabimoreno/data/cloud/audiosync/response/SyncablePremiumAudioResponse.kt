package soy.gabimoreno.data.cloud.audiosync.response

data class SyncablePremiumAudioResponse(
    override val id: String = EMPTY_STRING,
    override val hasBeenListened: Boolean = false,
    override val markedAsFavorite: Boolean = false,
) : SyncableAudio

private const val EMPTY_STRING = ""
