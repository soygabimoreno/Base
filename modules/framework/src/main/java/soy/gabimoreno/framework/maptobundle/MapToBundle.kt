package soy.gabimoreno.framework.maptobundle

import android.os.Bundle

fun Map<String, Any>.mapToBundle(): Bundle {
    val bundle = Bundle()
    forEach { entry: Map.Entry<String, Any> ->
        val (key, value) = entry
        when (value) {
            is Int -> bundle.putInt(key, value)
            is String -> bundle.putString(key, value)
            is Double -> bundle.putDouble(key, value)
            is Float -> bundle.putFloat(key, value)
            is Boolean -> bundle.putBoolean(key, value)
        }
    }
    return bundle
}
