package soy.gabimoreno.domain.usecase

import arrow.core.Either
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import javax.inject.Inject

class DeletePlaylistItemByIdUseCase @Inject constructor(
    private val playlistRepository: PlaylistRepository
) {
    suspend operator fun invoke(audioItemId: String, playlistId: Int): Either<Throwable, Unit> {
        return playlistRepository.deletePlaylistItemById(audioItemId, playlistId)
    }
}
