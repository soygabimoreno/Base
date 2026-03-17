package soy.gabimoreno.domain.usecase

import android.content.Context
import soy.gabimoreno.framework.datastore.dataStoreShouldIShowInAppReview
import javax.inject.Inject

class CheckShouldIShowInAppReviewUseCase
    @Inject
    constructor(
        private val context: Context,
    ) {
        suspend operator fun invoke(): Boolean = context.dataStoreShouldIShowInAppReview()
    }
