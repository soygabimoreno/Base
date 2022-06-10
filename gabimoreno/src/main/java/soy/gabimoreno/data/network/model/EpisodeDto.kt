package soy.gabimoreno.data.network.model

import com.google.gson.annotations.SerializedName
import soy.gabimoreno.domain.model.Episode

data class EpisodeDto(
    val id: String,
    val link: String?,
    val audio: String,
    val image: String,
    val podcast: PodcastDto,
    val thumbnail: String,
    @SerializedName("pub_date_ms")
    val pubDateMS: Long,
    @SerializedName("title_original")
    val titleOriginal: String,
    @SerializedName("listennotes_url")
    val listennotesURL: String,
    @SerializedName("audio_length_sec")
    val audioLengthSec: Long,
    @SerializedName("explicit_content")
    val explicitContent: Boolean,
    @SerializedName("description_original")
    val descriptionOriginal: String,
) {

    fun asDomainModel() = Episode(
        id,
        link,
        audio,
        image,
        podcast.asDomainModel(),
        thumbnail,
        pubDateMS,
        titleOriginal,
        listennotesURL,
        audioLengthSec,
        explicitContent,
        descriptionOriginal
    )
}
