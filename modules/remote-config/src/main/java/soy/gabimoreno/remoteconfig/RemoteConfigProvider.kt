package soy.gabimoreno.remoteconfig

interface RemoteConfigProvider {
    fun isFeatureEnabled(remoteConfigName: RemoteConfigName): Boolean
    fun getTokenCredentials(): TokenCredentials
    fun getTrialEmail(): String
    fun getTrialPassword(): String
}
