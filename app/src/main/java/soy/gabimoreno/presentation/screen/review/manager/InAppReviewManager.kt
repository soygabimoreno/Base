package soy.gabimoreno.presentation.screen.review.manager

import android.app.Activity
import kotlinx.coroutines.flow.SharedFlow

interface InAppReviewManager {
    val inAppPreviewEvents: SharedFlow<InAppReviewEvent>
    suspend fun onInAppReviewClicked(activity: Activity)
}
