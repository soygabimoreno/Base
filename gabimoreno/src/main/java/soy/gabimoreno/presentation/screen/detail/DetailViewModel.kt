package soy.gabimoreno.presentation.screen.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import soy.gabimoreno.R
import soy.gabimoreno.data.tracker.Tracker
import soy.gabimoreno.data.tracker.domain.PLAY_PAUSE
import soy.gabimoreno.data.tracker.domain.PlayPause
import soy.gabimoreno.data.tracker.main.DetailTrackerEvent.*
import soy.gabimoreno.data.tracker.toMap
import soy.gabimoreno.domain.model.Episode
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val tracker: Tracker
) : ViewModel() {

    fun onViewScreen(episode: Episode) {
        tracker.trackEvent(ViewScreen(episode.toMap()))
    }

    fun onPlayPauseClicked(
        episode: Episode,
        playPause: PlayPause
    ) {
        tracker.trackEvent(
            ClickPlayPause(
                episode.toMap() +
                    mapOf(
                        PLAY_PAUSE to playPause.toString()
                    )
            )
        )
    }

    fun onShareClicked(
        context: Context,
        episode: Episode
    ) {
        tracker.trackEvent(ClickShare(episode.toMap()))
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

    fun onInfoClicked(
        context: Context,
        episode: Episode
    ) {
        tracker.trackEvent(ClickInfo(episode.toMap()))
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(episode.url))
        context.startActivity(webIntent)
    }
}
