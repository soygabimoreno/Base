package soy.gabimoreno.framework.intent

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import javax.inject.Inject

class StartChooser @Inject constructor() {

    operator fun invoke(
        context: Context,
        @StringRes chooserTitleResId: Int,
        title: String,
        url: String
    ) {
        val text = context.getString(
            chooserTitleResId,
            title,
            url
        )
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TITLE, title)
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }
}
