package soy.gabimoreno.framework.maptobundle

import android.os.Bundle

fun Map<String, Any>.mapToBundleForTracking(): Bundle {
    val bundle = Bundle()
    forEach { entry: Map.Entry<String, Any> ->
        val (key, value) = entry
        bundle.putString(key, value.toString())
    }
    return bundle
}
