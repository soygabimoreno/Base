package soy.gabimoreno.domain.usecase

import android.content.Context
import kotlinx.coroutines.flow.first
import soy.gabimoreno.domain.repository.podcast.PodcastRepository
import soy.gabimoreno.framework.datastore.getEmail
import javax.inject.Inject

class MarkPodcastAsListenedUseCase
    @Inject
    constructor(
        private val context: Context,
        private val podcastRepository: PodcastRepository,
    ) {
        suspend operator fun invoke(
            podcastId: String,
            hasBeenListened: Boolean,
        ) {
            val email = context.getEmail().first()
            podcastRepository.markPodcastAsListened(
                podcastId = podcastId,
                email = email,
                hasBeenListened = hasBeenListened,
            )
        }
    }
