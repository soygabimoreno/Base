package soy.gabimoreno.domain.usecase

import android.content.Context
import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.domain.repository.senior.SeniorRepository
import soy.gabimoreno.framework.datastore.getEmail
import javax.inject.Inject

class GetSeniorStreamUseCase
@Inject
constructor(
    private val context: Context,
    private val seniorRepository: SeniorRepository,
) {
    suspend operator fun invoke(): Either<Throwable, Flow<List<Episode>>> {
        val email = context.getEmail().first()
        return seniorRepository.getEpisodesStream(
            email = email,
        )
    }
}
