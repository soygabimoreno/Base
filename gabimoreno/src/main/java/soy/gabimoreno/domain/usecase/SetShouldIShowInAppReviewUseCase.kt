package soy.gabimoreno.domain.usecase

import android.content.Context
import soy.gabimoreno.framework.datastore.setDataStoreShouldIShowInAppReview
import javax.inject.Inject

class SetShouldIShowInAppReviewUseCase
    @Inject
    constructor(
        private val context: Context,
    ) {
        suspend operator fun invoke(counterAudios: Int) {
            context.setDataStoreShouldIShowInAppReview(counterAudios)
        }
    }
