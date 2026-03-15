package soy.gabimoreno.domain.usecase

import android.content.Context
import kotlinx.coroutines.flow.first
import soy.gabimoreno.domain.repository.audiocourse.AudioCourseRepository
import soy.gabimoreno.framework.datastore.getEmail
import javax.inject.Inject

class SetAllAudiocoursesAsUnlistenedUseCase
    @Inject
    constructor(
        private val audioCourseRepository: AudioCourseRepository,
        private val context: Context,
    ) {
        suspend operator fun invoke() {
            val email = context.getEmail().first()
            audioCourseRepository.markAllAudioCourseItemsAsUnlistened(email = email)
        }
    }
