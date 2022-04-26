package soy.gabimoreno.bike.framework

import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

class PermissionRequester(
    activity: ComponentActivity,
    private val permission: String,
    private val onShowRationale: () -> Unit,
    private val onDenied: () -> Unit
) {
    private var onGranted: () -> Unit = {}
    private val permissionLauncher =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> onGranted()
                activity.shouldShowRequestPermissionRationale(permission) -> onShowRationale()
                else -> onDenied()
            }

        }

    fun runWithPermission(onGranted: () -> Unit) {
        this.onGranted = onGranted
        permissionLauncher.launch(permission)
    }
}
