package soy.gabimoreno.domain.usecase

import arrow.core.Either
import soy.gabimoreno.domain.model.audio.Audio
import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import soy.gabimoreno.domain.repository.podcast.PodcastRepository
import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
import soy.gabimoreno.domain.util.AudioItemType
import soy.gabimoreno.domain.util.audioItemTypeDetector
import javax.inject.Inject

class GetAudioByIdUseCase
    @Inject
    constructor(
        private val audioCoursesRepository: AudioCoursesRepository,
        private val podcastRepository: PodcastRepository,
        private val premiumAudiosRepository: PremiumAudiosRepository,
    ) {
        suspend operator fun invoke(audioId: String): Either<Throwable, Audio> =
            when (audioItemTypeDetector(audioId)) {
                AudioItemType.AUDIO_COURSE -> {
                    audioCoursesRepository.getAudioCourseItemById(audioId)
                }

                AudioItemType.PREMIUM_AUDIO -> {
                    premiumAudiosRepository.getPremiumAudioById(audioId)
                }

                AudioItemType.PODCAST -> {
                    podcastRepository.getPodcastById(audioId)
                }
            }
    }
