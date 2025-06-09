package soy.gabimoreno.domain.usecase

import arrow.core.Either
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import javax.inject.Inject

class SetPlaylistItemsUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {
    suspend operator fun invoke(
        playlistItemId: String,
        playlistIds: List<Int>
    ): Either<Throwable, List<Long>> {
        return repository.upsertPlaylistItems(playlistItemId, playlistIds)
    }
}
