package soy.gabimoreno

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import soy.gabimoreno.framework.KLog

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        KLog.launch(BuildConfig.DEBUG)
        initFirebase()
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(this)
    }
}
