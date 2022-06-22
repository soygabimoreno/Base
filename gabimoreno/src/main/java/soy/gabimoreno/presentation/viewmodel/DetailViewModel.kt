package soy.gabimoreno.presentation.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import soy.gabimoreno.R
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.domain.model.Episode
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val tracker: Tracker
) : ViewModel() {

    init {
        // TODO
//        tracker.trackEvent(DetailTrackerEvent.ScreenDetail(mapOf("ExampleKey" to "ExampleValue")))
    }

    fun sharePodcastEpisode(
        context: Context,
        episode: Episode
    ) {
        val text = context.getString(
            R.string.share_podcast_content,
            episode.titleOriginal,
            episode.listennotesURL
        )
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TITLE, episode.titleOriginal)
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    fun openListenNotesURL(
        context: Context,
        episode: Episode
    ) {
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(episode.link))
        context.startActivity(webIntent)
    }
}
