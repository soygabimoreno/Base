package soy.gabimoreno.paparazzi

import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import org.junit.Rule

class AudioBottomBarKtTest {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar"
    )

//    @Test
//    fun launchComposable() {
//
//        // TODO: Check the SDK configuration
//
//        paparazzi.snapshot {
//            AudioBottomBarStatelessContent(
//                audio = Episode(
//                    id = "1",
//                    url = "",
//                    audioUrl = "",
//                    imageUrl = "https://picsum.photos/200",
//                    saga = Saga("This is publisher", "This is podcast title"),
//                    thumbnailUrl = "https://picsum.photos/200",
//                    pubDateMillis = 0,
//                    title = "This is a title",
//                    audioLengthInSeconds = 2700,
//                    description = "This is a description"
//                ),
//                xOffset = 0,
//                icon = R.drawable.ic_baseline_play_arrow_24,
//                onTooglePlaybackState = { },
//                onTap = { }
//            )
//        }
//    }
}
