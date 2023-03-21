package soy.gabimoreno.presentation.ui.button

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.ui.PreviewContent

class CommonButtonKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun commonButtonTest() {
        val text = "Play"
        composeTestRule.setContent {
            PreviewContent {
                CommonButton(
                    text,
                    height = Spacing.s48,
                    MaterialTheme.colors.primary
                ) {}
            }
        }

        composeTestRule.onNodeWithTag(COMMON_BUTTON_BOX_ID)
            .assertIsDisplayed()
//            .onChild()
//            .assert(hasText(text))
    }
}
