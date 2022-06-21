package soy.gabimoreno

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import soy.gabimoreno.framework.KLog

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        KLog.launch(BuildConfig.DEBUG)
    }
}
