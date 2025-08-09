package soy.gabimoreno.domain.usecase

import android.content.Context
import kotlinx.coroutines.flow.first
import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import soy.gabimoreno.domain.repository.podcast.PodcastRepository
import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
import soy.gabimoreno.domain.util.AudioItemType
import soy.gabimoreno.domain.util.audioItemTypeDetector
import soy.gabimoreno.framework.datastore.getEmail
import javax.inject.Inject

class UpdateAudioItemFavoriteStateUseCase
    @Inject
    constructor(
        private val audioCoursesRepository: AudioCoursesRepository,
        private val context: Context,
        private val podcastRepository: PodcastRepository,
        private val premiumAudioCoursesRepository: PremiumAudiosRepository,
    ) {
        suspend operator fun invoke(
            idAudioItem: String,
            markedAsFavorite: Boolean,
        ) {
            val email = context.getEmail().first()
            when (audioItemTypeDetector(idAudioItem)) {
                AudioItemType.AUDIO_COURSE ->
                    audioCoursesRepository.updateMarkedAsFavorite(
                        audioCourseId = idAudioItem,
                        email = email,
                        isFavorite = markedAsFavorite,
                    )

                AudioItemType.PREMIUM_AUDIO ->
                    premiumAudioCoursesRepository.markPremiumAudioAsFavorite(
                        email = email,
                        premiumAudioId = idAudioItem,
                        isFavorite = markedAsFavorite,
                    )

                AudioItemType.PODCAST ->
                    podcastRepository.updateMarkedAsFavorite(
                        podcastId = idAudioItem,
                        email = email,
                        isFavorite = markedAsFavorite,
                    )
            }
        }
    }
