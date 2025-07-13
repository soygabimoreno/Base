package soy.gabimoreno.domain.usecase

import android.content.Context
import kotlinx.coroutines.flow.first
import soy.gabimoreno.domain.repository.audiocourses.AudioCoursesRepository
import soy.gabimoreno.framework.datastore.getEmail
import javax.inject.Inject

class SetAllAudiocoursesAsUnlistenedUseCase @Inject constructor(
    private val audioCoursesRepository: AudioCoursesRepository,
    private val context: Context,
) {
    suspend operator fun invoke() {
        val email = context.getEmail().first()
        audioCoursesRepository.markAllAudioCourseItemsAsUnlistened(email = email)
    }
}
