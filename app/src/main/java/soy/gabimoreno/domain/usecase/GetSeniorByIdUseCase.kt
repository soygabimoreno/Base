package soy.gabimoreno.domain.usecase

import arrow.core.Either
import soy.gabimoreno.domain.model.podcast.Episode
import soy.gabimoreno.domain.repository.senioraudio.SeniorAudioRepository
import javax.inject.Inject

class GetSeniorByIdUseCase
    @Inject
    constructor(
        private val seniorAudioRepository: SeniorAudioRepository,
    ) {
        suspend operator fun invoke(podcastId: String): Either<Throwable, Episode> =
            seniorAudioRepository.getPodcastById(podcastId)
    }
