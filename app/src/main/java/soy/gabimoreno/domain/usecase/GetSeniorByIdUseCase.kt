package soy.gabimoreno.domain.usecase

import arrow.core.Either
import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.domain.repository.senior.SeniorRepository
import javax.inject.Inject

class GetSeniorByIdUseCase
    @Inject
    constructor(
        private val seniorRepository: SeniorRepository,
    ) {
        suspend operator fun invoke(podcastId: String): Either<Throwable, Episode> =
            seniorRepository.getPodcastById(podcastId)
    }
