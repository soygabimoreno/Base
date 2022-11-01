package soy.gabimoreno

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.domain.usecase.SubscribeToTopicUseCase
import soy.gabimoreno.framework.KLog
import soy.gabimoreno.framework.notification.TOPIC_NEW_AUDIO
import soy.gabimoreno.framework.notification.createNotificationChannel
import javax.inject.Inject


@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var subscribeToTopicUseCase: SubscribeToTopicUseCase

    @Inject
    lateinit var tracker: Tracker

    override fun onCreate() {
        super.onCreate()
        initDebugLogs()
        initFirebase()
        initNotifications()
    }

    private fun initDebugLogs() {
        KLog.launch(BuildConfig.DEBUG)
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(this)
        subscribeToTopicUseCase(TOPIC_NEW_AUDIO)
    }

    private fun initNotifications() {
        createNotificationChannel(this)
    }
}
