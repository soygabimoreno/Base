package soy.gabimoreno.domain.usecase

import arrow.core.Either
import soy.gabimoreno.domain.model.content.Playlist
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import javax.inject.Inject

class UpsertPlaylistsUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {
    suspend operator fun invoke(
        playlists: List<Playlist>
    ): Either<Throwable, Unit> {
        return repository.upsertPlaylists(playlists)
    }
}
