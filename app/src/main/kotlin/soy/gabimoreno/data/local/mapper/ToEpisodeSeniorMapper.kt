package soy.gabimoreno.data.local.mapper

import soy.gabimoreno.data.local.podcast.model.SeniorAudioDbModel
import soy.gabimoreno.domain.model.content.SeniorAudio

fun SeniorAudioDbModel.toSeniorAudio() =
    SeniorAudio(
        id = id,
        title = title,
        description = description,
        saga = saga,
        url = url,
        audioUrl = audioUrl,
        imageUrl = imageUrl,
        thumbnailUrl = thumbnailUrl,
        pubDateMillis = pubDateMillis,
        audioLengthInSeconds = audioLengthInSeconds,
        hasBeenListened = hasBeenListened,
        markedAsFavorite = markedAsFavorite,
    )
