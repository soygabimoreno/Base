package soy.gabimoreno.presentation.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

internal class SelectableEmphasisTextTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun checkSelectableEmphasisTextIsDisplayed() {
        val expectedText = "Hello, World!"

        composeTestRule.setContent {
            SelectableEmphasisText(
                text = expectedText
            )
        }

        composeTestRule
            .onNodeWithText(expectedText)
            .assertExists()
            .assertIsDisplayed()
    }
}
