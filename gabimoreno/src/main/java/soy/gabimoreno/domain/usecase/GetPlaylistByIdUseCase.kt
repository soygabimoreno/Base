package soy.gabimoreno.domain.usecase

import arrow.core.Either
import soy.gabimoreno.domain.model.content.Playlist
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import javax.inject.Inject

class GetPlaylistByIdUseCase
    @Inject
    constructor(
        private val playlistRepository: PlaylistRepository,
    ) {
        suspend operator fun invoke(idPlaylist: Int): Either<Throwable, Playlist?> =
            playlistRepository.getPlaylistById(idPlaylist)
    }
