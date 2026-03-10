package soy.gabimoreno.domain.usecase

import android.content.Context
import kotlinx.coroutines.flow.first
import soy.gabimoreno.domain.repository.podcast.PodcastRepository
import soy.gabimoreno.framework.datastore.getEmail
import javax.inject.Inject

class SetAllPodcastAsUnlistenedUseCase
    @Inject
    constructor(
        private val context: Context,
        private val podcastRepository: PodcastRepository,
    ) {
        suspend operator fun invoke() {
            val email = context.getEmail().first()
            podcastRepository.markAllPodcastAsUnlistened(email = email)
        }
    }
