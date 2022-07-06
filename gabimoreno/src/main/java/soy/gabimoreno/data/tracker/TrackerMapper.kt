package soy.gabimoreno.data.tracker

import soy.gabimoreno.data.tracker.domain.EPISODE_ID
import soy.gabimoreno.data.tracker.domain.EPISODE_TITLE
import soy.gabimoreno.domain.model.Episode

fun Episode.toMap() = mapOf(
    EPISODE_ID to id,
    EPISODE_TITLE to title,
)
