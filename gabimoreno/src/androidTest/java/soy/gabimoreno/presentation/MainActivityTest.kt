package soy.gabimoreno.presentation

import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import soy.gabimoreno.data.MockWebServerRule
import soy.gabimoreno.data.fromJson
import soy.gabimoreno.data.network.model.Category
import soy.gabimoreno.domain.repository.ContentRepository
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val mockWebServerRule = MockWebServerRule()

    @get:Rule(order = 2)
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Inject
    lateinit var contentRepository: ContentRepository

    @Before
    fun setUp() {
        mockWebServerRule.server.enqueue(MockResponse().fromJson("premium_posts.json"))

        hiltAndroidRule.inject()

        val resource = OkHttp3IdlingResource.create("OkHttp", okHttpClient)
        IdlingRegistry.getInstance().register(resource)
    }

    @Test
    fun check_mock_web_server_is_working() = runTest {
        val categories = Category.values().toList()
        contentRepository.getPosts(categories)
            .fold({
                      throw Exception(it.toString())
                  }, { posts ->
                      posts.size shouldBeEqualTo 4
                      val post = posts[0]
                      post.id shouldBeEqualTo FIRST_POST_ID
                  }
            )
    }
}

private const val FIRST_POST_ID = 5436L
