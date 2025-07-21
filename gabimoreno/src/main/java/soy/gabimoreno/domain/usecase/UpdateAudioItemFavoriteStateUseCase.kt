package soy.gabimoreno.domain.usecase

import android.content.Context
import kotlinx.coroutines.flow.first
import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
import soy.gabimoreno.framework.datastore.getEmail
import javax.inject.Inject

class UpdateAudioItemFavoriteStateUseCase
    @Inject
    constructor(
        private val audioCoursesRepository: AudioCoursesRepository,
        private val context: Context,
        private val premiumAudioCoursesRepository: PremiumAudiosRepository,
    ) {
        suspend operator fun invoke(
            idAudioItem: String,
            markedAsFavorite: Boolean,
        ) {
            val email = context.getEmail().first()
            if (idAudioItem.contains("-")) {
                audioCoursesRepository.updateMarkedAsFavorite(
                    audioCourseId = idAudioItem,
                    email = email,
                    isFavorite = markedAsFavorite,
                )
            } else {
                premiumAudioCoursesRepository.markPremiumAudioAsFavorite(
                    email = email,
                    premiumAudioId = idAudioItem,
                    isFavorite = markedAsFavorite,
                )
            }
        }
    }
