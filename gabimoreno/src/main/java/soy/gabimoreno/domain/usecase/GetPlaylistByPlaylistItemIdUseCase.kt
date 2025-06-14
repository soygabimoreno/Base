package soy.gabimoreno.domain.usecase

import arrow.core.Either
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import javax.inject.Inject

class GetPlaylistByPlaylistItemIdUseCase @Inject constructor(
    private val playlistRepository: PlaylistRepository
) {
    suspend operator fun invoke(audioItemId: String): Either<Throwable, List<Int>> {
        return playlistRepository.getPlaylistIdsByItemId(audioItemId)
    }
}
