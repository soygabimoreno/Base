package soy.gabimoreno.domain.usecase

import arrow.core.Either
import soy.gabimoreno.domain.model.content.Playlist
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import javax.inject.Inject

class InsertPlaylistUseCase @Inject constructor(
    private val playlistDRepository: PlaylistRepository
) {
    suspend operator fun invoke(name: String, description: String): Either<Throwable, Playlist> {
        return playlistDRepository.insertPlaylist(name = name, description = description)
    }
}
