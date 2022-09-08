package soy.gabimoreno.data.network.mapper

import soy.gabimoreno.domain.model.audio.Saga
import soy.gabimoreno.domain.model.content.Post
import soy.gabimoreno.domain.model.content.PremiumAudio

fun List<Post>.toPremiumAudios(): List<PremiumAudio> {
    val premiumAudios = mutableListOf<PremiumAudio>()
    this.forEach { post ->
        if (
            post.audioUrl != null &&
            post.subcategoryTitle != null
        ) {
            premiumAudios.add(
                PremiumAudio(
                    id = post.getPremiumAudioId(),
                    title = post.title,
                    description = post.content,
                    saga = Saga(author = post.author, post.subcategoryTitle),
                    url = post.url,
                    audioUrl = post.audioUrl,
                    imageUrl = "https://gabimoreno.soy/wp-content/uploads/GABI-MORENO-Premium-sample-mp3-image.png", // TODO: This is fake content for now
                    thumbnailUrl = "https://gabimoreno.soy/wp-content/uploads/GABI-MORENO-Premium-sample-mp3-image.png", // TODO: This is fake content for now
                    pubDateMillis = post.pubDateMillis,
                    audioLengthInSeconds = EMPTY_AUDIO_LENGTH_IN_SECONDS, // TODO: This is unknown for now
                )
            )
        }
    }
    return premiumAudios
}

internal const val EMPTY_AUDIO_LENGTH_IN_SECONDS = 0
