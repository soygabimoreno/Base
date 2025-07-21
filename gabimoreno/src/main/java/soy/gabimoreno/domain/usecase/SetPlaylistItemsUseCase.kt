package soy.gabimoreno.domain.usecase

import android.content.Context
import arrow.core.Either
import kotlinx.coroutines.flow.first
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import soy.gabimoreno.framework.datastore.getEmail
import javax.inject.Inject

class SetPlaylistItemsUseCase
    @Inject
    constructor(
        private val context: Context,
        private val repository: PlaylistRepository,
    ) {
        suspend operator fun invoke(
            audioId: String,
            playlistIds: List<Int>,
        ): Either<Throwable, List<Long>> {
            val email = context.getEmail().first()
            return repository.upsertPlaylistItems(audioId, playlistIds, email)
        }
    }
