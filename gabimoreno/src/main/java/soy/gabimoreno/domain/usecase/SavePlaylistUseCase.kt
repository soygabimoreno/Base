package soy.gabimoreno.domain.usecase

import arrow.core.Either
import soy.gabimoreno.domain.model.content.Playlist
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import javax.inject.Inject

class SavePlaylistUseCase @Inject constructor(
    private val playlistRepository: PlaylistRepository
) {
    suspend operator fun invoke(playlist: Playlist): Either<Throwable, Unit> {
        return playlistRepository.savePlaylist(playlist)
    }
}
