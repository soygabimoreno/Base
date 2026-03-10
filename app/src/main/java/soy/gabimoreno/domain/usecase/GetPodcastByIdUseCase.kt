package soy.gabimoreno.domain.usecase

import arrow.core.Either
import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.domain.repository.podcast.PodcastRepository
import javax.inject.Inject

class GetPodcastByIdUseCase
    @Inject
    constructor(
        private val podcastRepository: PodcastRepository,
    ) {
        suspend operator fun invoke(podcastId: String): Either<Throwable, Episode> =
            podcastRepository.getPodcastById(podcastId)
    }
