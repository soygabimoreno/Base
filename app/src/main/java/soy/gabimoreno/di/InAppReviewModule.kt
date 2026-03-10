package soy.gabimoreno.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import soy.gabimoreno.presentation.screen.review.manager.DefaultInAppReviewManager
import soy.gabimoreno.presentation.screen.review.manager.InAppReviewManager

@Module
@InstallIn(SingletonComponent::class)
abstract class InAppReviewModule {
    @Binds
    abstract fun bindInAppReviewManagerHelper(
        inAppReviewManager: DefaultInAppReviewManager,
    ): InAppReviewManager
}
