package soy.gabimoreno.remoteconfig

import com.google.firebase.remoteconfig.BuildConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.internal.ConfigFetchHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRemoteConfigProvider @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) : RemoteConfigProvider {

    init {
        initRemoteConfig()
        forceFetchNewData()
    }

    private fun initRemoteConfig() {
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        val minimumFetchIntervalInSeconds =
            if (BuildConfig.DEBUG) {
                0
            } else {
                ConfigFetchHandler.DEFAULT_MINIMUM_FETCH_INTERVAL_IN_SECONDS
            }
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(minimumFetchIntervalInSeconds)
            .build()
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
    }

    private fun forceFetchNewData() {
        firebaseRemoteConfig.fetch(1L).addOnCompleteListener {
            if (it.isSuccessful) {
                firebaseRemoteConfig.activate()
            }
        }
    }

    override fun isFeatureEnabled(remoteConfigName: RemoteConfigName): Boolean {
        return firebaseRemoteConfig.getBoolean(remoteConfigName.name.lowercase())
    }

    override fun getTokenCredentials(): TokenCredentials {
        val username = firebaseRemoteConfig.getString(TOKEN_CREDENTIAL_USERNAME)
        val password = firebaseRemoteConfig.getString(TOKEN_CREDENTIAL_PASSWORD)
        return TokenCredentials(username, password)
    }
}

private const val TOKEN_CREDENTIAL_USERNAME = "TOKEN_CREDENTIAL_USERNAME"
private const val TOKEN_CREDENTIAL_PASSWORD = "TOKEN_CREDENTIAL_PASSWORD"
