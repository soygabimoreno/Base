package soy.gabimoreno.presentation.screen.review.manager

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import soy.gabimoreno.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class DefaultInAppReviewManager @Inject constructor(
    @ApplicationContext private val context: Context,
) : InAppReviewManager {

    private val _inAppPreviewEvents = MutableSharedFlow<InAppReviewEvent>(
        replay = 0,
        extraBufferCapacity = 1
    )
    override val inAppPreviewEvents: SharedFlow<InAppReviewEvent> =
        _inAppPreviewEvents.asSharedFlow()

    override suspend fun onInAppReviewClicked(activity: Activity) {
        val reviewManager = if (BuildConfig.DEBUG) {
            FakeReviewManager(context)
        } else {
            ReviewManagerFactory.create(context)
        }

        try {
            val reviewInfo = reviewManager.requestReviewFlow().await()
            reviewManager.launchReviewFlow(activity, reviewInfo).await()
            _inAppPreviewEvents.emit(InAppReviewEvent.Completed)
        } catch (e: Exception) {
            Log.w("InAppReviewManager", "InAppReviewManager failed:", e)
            _inAppPreviewEvents.emit(InAppReviewEvent.Failed)
        }
    }

    private suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { continuation ->
        addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resume(task.result)
            } else {
                continuation.resumeWithException(
                    task.exception ?: RuntimeException("Task failed without exception")
                )
            }
        }
    }
}
