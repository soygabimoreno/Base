package soy.gabimoreno.data.local.mapper

import soy.gabimoreno.data.local.premiumaudio.PremiumAudioDbModel
import soy.gabimoreno.domain.model.content.PremiumAudio

fun PremiumAudioDbModel.toPremiumAudio() = PremiumAudio(
    id,
    title,
    description,
    saga,
    url,
    audioUrl,
    imageUrl,
    thumbnailUrl,
    pubDateMillis,
    audioLengthInSeconds,
    hasBeenListened,
    category,
    excerpt,
)
