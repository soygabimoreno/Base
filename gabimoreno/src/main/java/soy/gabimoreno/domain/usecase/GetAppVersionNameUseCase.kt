package soy.gabimoreno.domain.usecase

import soy.gabimoreno.domain.interoperability.BuildConfigBridge
import javax.inject.Inject

class GetAppVersionNameUseCase @Inject constructor(
    private val buildConfigBridge: BuildConfigBridge,
) {
    operator fun invoke(): String = "v${buildConfigBridge.appVersionName}"
}
