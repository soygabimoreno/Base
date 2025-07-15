package soy.gabimoreno.domain.usecase

import android.content.Context
import arrow.core.Either
import kotlinx.coroutines.flow.first
import soy.gabimoreno.domain.model.content.Playlist
import soy.gabimoreno.domain.repository.playlist.PlaylistRepository
import soy.gabimoreno.framework.datastore.getEmail
import javax.inject.Inject

class GetAllPlaylistUseCase @Inject constructor(
    private val context: Context,
    private val playlistRepository: PlaylistRepository
) {
    suspend operator fun invoke(): Either<Throwable, List<Playlist>> {
        val email = context.getEmail().first()
        return playlistRepository.getAllPlaylists(email)
    }
}
