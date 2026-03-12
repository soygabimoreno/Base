package soy.gabimoreno.domain.usecase

import android.content.Context
import kotlinx.coroutines.flow.first
import soy.gabimoreno.domain.repository.audiocourses.AudioCourseRepository
import soy.gabimoreno.framework.datastore.getEmail
import javax.inject.Inject

class MarkAudioCourseItemAsListenedUseCase
    @Inject
    constructor(
        private val audioCourseRepository: AudioCourseRepository,
        private val context: Context,
    ) {
        suspend operator fun invoke(
            idAudioCourseItem: String,
            hasBeenListened: Boolean,
        ) {
            val email = context.getEmail().first()
            audioCourseRepository.markAudioCourseItemAsListened(
                audioCourseId = idAudioCourseItem,
                email = email,
                hasBeenListened = hasBeenListened,
            )
        }
    }
