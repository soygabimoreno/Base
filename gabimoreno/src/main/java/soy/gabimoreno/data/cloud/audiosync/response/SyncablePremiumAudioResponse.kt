package soy.gabimoreno.data.cloud.audiosync.response

data class SyncablePremiumAudioResponse(
    override val id: String = "",
    override val hasBeenListened: Boolean = false,
    override val markedAsFavorite: Boolean = false,
) : SyncableAudio
