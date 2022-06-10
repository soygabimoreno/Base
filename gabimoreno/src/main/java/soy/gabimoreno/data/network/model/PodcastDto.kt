package soy.gabimoreno.data.network.model

import com.google.gson.annotations.SerializedName
import soy.gabimoreno.domain.model.Podcast

data class PodcastDto(
    val id: String,
    val image: String,
    val thumbnail: String,
    @SerializedName("title_original")
    val titleOriginal: String,
    @SerializedName("listennotes_url")
    val listennotesURL: String,
    @SerializedName("publisher_original")
    val publisherOriginal: String
) {

    fun asDomainModel() = Podcast(
        id,
        image,
        thumbnail,
        titleOriginal,
        listennotesURL,
        publisherOriginal
    )
}
