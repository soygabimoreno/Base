package soy.gabimoreno.domain.usecase

import android.content.Context
import arrow.core.Either
import kotlinx.coroutines.flow.first
import soy.gabimoreno.domain.model.content.PlaylistAudioItem
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import soy.gabimoreno.framework.datastore.getEmail
import javax.inject.Inject

class UpdatePlaylistItemsUseCase @Inject constructor(
    private val context: Context,
    private val repository: PlaylistRepository
) {
    suspend operator fun invoke(
        playlistId: Int,
        playlistItems: List<PlaylistAudioItem>
    ): Either<Throwable, Unit> {
        val email = context.getEmail().first()
        return repository.updatePlaylistItems(
            playlistId = playlistId,
            playlistItems = playlistItems,
            email = email
        )
    }
}
