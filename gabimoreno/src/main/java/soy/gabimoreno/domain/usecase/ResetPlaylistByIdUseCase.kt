package soy.gabimoreno.domain.usecase

import arrow.core.Either
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import javax.inject.Inject

class ResetPlaylistByIdUseCase @Inject constructor(
    private val playlistDRepository: PlaylistRepository
) {
    suspend operator fun invoke(idPlaylist: Int): Either<Throwable, Unit> {
        return playlistDRepository.resetPlaylistById(idPlaylist)
    }
}
