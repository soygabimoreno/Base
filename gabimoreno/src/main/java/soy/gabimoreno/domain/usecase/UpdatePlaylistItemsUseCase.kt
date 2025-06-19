package soy.gabimoreno.domain.usecase

import arrow.core.Either
import soy.gabimoreno.domain.model.content.PlaylistAudioItem
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import javax.inject.Inject

class UpdatePlaylistItemsUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {
    suspend operator fun invoke(
        playlistId: Int,
        playlistItems: List<PlaylistAudioItem>
    ): Either<Throwable, Unit> {
        return repository.updatePlaylistItems(
            playlistId = playlistId,
            playlistItems = playlistItems
        )
    }
}
