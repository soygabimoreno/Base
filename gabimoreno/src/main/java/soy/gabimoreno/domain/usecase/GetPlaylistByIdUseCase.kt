package soy.gabimoreno.domain.usecase

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import soy.gabimoreno.domain.model.content.Playlist
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import javax.inject.Inject

class GetPlaylistByIdUseCase @Inject constructor(
    private val playlistRepository: PlaylistRepository
) {
    operator fun invoke(idPlaylist: Int): Either<Throwable, Flow<Playlist?>> {
        return playlistRepository.getPlaylistById(idPlaylist)
    }
}
