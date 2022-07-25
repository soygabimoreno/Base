package soy.gabimoreno.data.tracker

import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_EPISODE_ID
import soy.gabimoreno.data.tracker.domain.TRACKER_KEY_EPISODE_TITLE
import soy.gabimoreno.domain.model.Episode

fun Episode.toMap() = mapOf(
    TRACKER_KEY_EPISODE_ID to id,
    TRACKER_KEY_EPISODE_TITLE to title,
)
