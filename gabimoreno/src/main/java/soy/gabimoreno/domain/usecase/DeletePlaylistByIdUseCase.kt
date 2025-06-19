package soy.gabimoreno.domain.usecase

import arrow.core.Either
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import javax.inject.Inject

class DeletePlaylistByIdUseCase @Inject constructor(
    private val playlistRepository: PlaylistRepository
) {
    suspend operator fun invoke(playlistId: Int): Either<Throwable, Unit> {
        return playlistRepository.deletePlaylistById(playlistId)
    }
}
