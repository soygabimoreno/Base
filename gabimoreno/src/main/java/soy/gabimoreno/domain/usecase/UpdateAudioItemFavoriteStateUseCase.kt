package soy.gabimoreno.domain.usecase

import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import soy.gabimoreno.domain.repository.premiumaudios.PremiumAudiosRepository
import javax.inject.Inject

class UpdateAudioItemFavoriteStateUseCase @Inject constructor(
    private val audioCoursesRepository: AudioCoursesRepository,
    private val premiumAudioCoursesRepository: PremiumAudiosRepository,
) {
    suspend operator fun invoke(idAudioItem: String, markedAsFavorite: Boolean) {
        if (idAudioItem.contains("-")) {
            audioCoursesRepository.updateMarkedAsFavorite(idAudioItem, markedAsFavorite)
        } else {
            premiumAudioCoursesRepository.markPremiumAudioAsFavorite(idAudioItem, markedAsFavorite)
        }
    }
}
