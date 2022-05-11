package soy.gabimoreno

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import soy.gabimoreno.domain.Audio
import soy.gabimoreno.framework.service.PlayerService
import soy.gabimoreno.ui.theme.BaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PlayerService.start(
            this, Audio(
                id = 1L,
                artist = "Foo Artist",
                filename = "Temporary file name",
                name = "Audio name",
                description = "Foo Description",
                thumbnailUrl = "https://gabinimble.com/wp-content/uploads/2019/03/Portada-GABI-NIMBLE-94-98-400x400.png",
                audioUrl = "https://gabinimble.com/wp-content/uploads/2019/03/GABI-NIMBLE-94-98.mp3"
            )
        )

        setContent {
            BaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BaseTheme {
        Greeting("Android")
    }
}
