package soy.gabimoreno.presentation.screen.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import soy.gabimoreno.R
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.main.DetailTrackerEvent.ClickPlayPause
import soy.gabimoreno.data.tracker.main.DetailTrackerEvent.ViewScreen
import soy.gabimoreno.domain.model.Episode
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val tracker: Tracker
) : ViewModel() {

    fun onViewScreen() {
        tracker.trackEvent(ViewScreen(mapOf()))
    }

    fun onPlayPauseClicked() {
        tracker.trackEvent(ClickPlayPause(mapOf()))
    }

    fun sharePodcastEpisode(
        context: Context,
        episode: Episode
    ) {
        val text = context.getString(
            R.string.share_podcast_content,
            episode.title,
            episode.url
        )
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TITLE, episode.title)
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
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(episode.url))
        context.startActivity(webIntent)
    }
}
