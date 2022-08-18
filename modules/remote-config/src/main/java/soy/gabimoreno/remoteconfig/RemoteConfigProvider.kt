package soy.gabimoreno.remoteconfig

interface RemoteConfigProvider {
    fun isFeatureEnabled(remoteConfigName: RemoteConfigName): Boolean
}
