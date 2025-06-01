package soy.gabimoreno.data.local.mapper

import soy.gabimoreno.data.local.premiumaudio.PremiumAudioDbModel
import soy.gabimoreno.domain.model.content.PremiumAudio

fun PremiumAudio.toPremiumAudioDbModel() = PremiumAudioDbModel(
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
    category = category,
    excerpt = excerpt,
    hasBeenListened = hasBeenListened,
)
