package soy.gabimoreno.framework.intent

import android.content.Context
import android.content.Intent
import android.net.Uri
import javax.inject.Inject

class StartActionView @Inject constructor() {

    operator fun invoke(
        context: Context,
        url: String,
    ) {
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(webIntent)
    }
}
