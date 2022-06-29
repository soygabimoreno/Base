package soy.gabimoreno.domain.interoperability

import soy.gabimoreno.BuildConfig
import javax.inject.Inject

class BuildConfigBridge @Inject constructor() {

    val appVersionName = BuildConfig.VERSION_NAME
}
