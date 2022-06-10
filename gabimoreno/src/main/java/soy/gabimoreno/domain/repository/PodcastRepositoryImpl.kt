package soy.gabimoreno.domain.repository

import soy.gabimoreno.data.datastore.PodcastDataStore
import soy.gabimoreno.data.network.service.PodcastService
import soy.gabimoreno.domain.model.Episode
import soy.gabimoreno.domain.model.Podcast
import soy.gabimoreno.domain.model.PodcastSearch
import soy.gabimoreno.error.Failure
import soy.gabimoreno.util.Either
import soy.gabimoreno.util.left
import soy.gabimoreno.util.right
import java.util.*

class PodcastRepositoryImpl(
    private val service: PodcastService,
    private val dataStore: PodcastDataStore
) : PodcastRepository {

    companion object {
        private const val TAG = "PodcastRepository"
    }

    override suspend fun getPodcast(): Either<Failure, PodcastSearch> {
        return try {
            val result = PodcastSearch(
                1,
                1,
                listOf(
                    buildEpisode(),
                    buildEpisode(),
                    buildEpisode(),
                    buildEpisode(),
                    buildEpisode(),
                    buildEpisode()
                )
            )
            right(result)


//            val canFetchAPI = dataStore.canFetchAPI()
//            if (canFetchAPI) {
//                    val podcastApiModel = service.getPodcast()
//                val result = podcastApiModel.toDomain()
//                dataStore.storePodcastSearchResult(result)
//                right(result)
//                    left (Failure.UnexpectedFailure)
//            } else {
//                right(dataStore.readLastPodcastSearchResult())
//            }
        } catch (e: Exception) {
            left(Failure.UnexpectedFailure)
        }
    }

    private fun buildEpisode() = Episode(
        id = "103",
        link = "https://gabimoreno.soy",
        audio = "https://anchor.fm/s/74bc02a4/podcast/play/52736678/https%3A%2F%2Fd3ctxlq1ktw2nl.cloudfront.net%2Fstaging%2F2022-4-29%2F7fd0eafe-18dd-adc1-24ff-7870d19ca3b1.mp3",
        image = "https://d3t3ozftmdmh3i.cloudfront.net/production/podcast_uploaded_episode/19484785/19484785-1653852932431-d51c81c5a5546.jpg",
        podcast = Podcast(
            id = "1234",  // TODO: Will be required ???
            image = "", // TODO: Fill these properties
            thumbnail = "",
            titleOriginal = "",
            listennotesURL = "",
            publisherOriginal = ""
        ),
        thumbnail = "https://d3t3ozftmdmh3i.cloudfront.net/production/podcast_uploaded_episode/19484785/19484785-1653852932431-d51c81c5a5546.jpg",
        pubDateMS = Date("Mon, 30 May 2022 04:00:20 GMT").time,
        titleOriginal = "103. Mi EXPERIENCIA tras 10 AÑOS trabajando como DESARROLLADOR ANDROID",
        listennotesURL = "https://anchor.fm/s/74bc02a4/podcast/rss",
        audioLengthSec = 3809,
        explicitContent = false,
        descriptionOriginal = "Este es un episodio especial. Te voy a contar mi experiencia después de estar trabajando 10 años como Desarrollador Android."

    )
}
