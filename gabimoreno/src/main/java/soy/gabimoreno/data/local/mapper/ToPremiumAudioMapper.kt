package soy.gabimoreno.data.local.mapper

import soy.gabimoreno.data.local.PremiumAudioDbModel
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
    category,
    excerpt
)
