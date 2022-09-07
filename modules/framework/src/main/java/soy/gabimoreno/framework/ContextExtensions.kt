package soy.gabimoreno.framework

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.toast(
    @StringRes resId: Int,
    duration: Int = Toast.LENGTH_SHORT,
) {
    Toast.makeText(
        this,
        resId,
        duration
    ).show()
}

fun Context.toast(
    message: CharSequence,
    duration: Int = Toast.LENGTH_SHORT,
) {
    Toast.makeText(
        this,
        message,
        duration
    ).show()
}
