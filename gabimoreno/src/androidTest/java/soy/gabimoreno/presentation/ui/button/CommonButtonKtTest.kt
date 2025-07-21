package soy.gabimoreno.presentation.ui.button

import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithTag
import org.amshove.kluent.shouldBe
import org.junit.Rule
import org.junit.Test
import soy.gabimoreno.presentation.theme.Orange
import soy.gabimoreno.presentation.theme.Spacing
import soy.gabimoreno.presentation.ui.PreviewContent

class CommonButtonKtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun checkBoxIsDisplayedInCommonButtonTest() {
        val text = "Play"
        composeTestRule.setContent {
            PreviewContent {
                CommonButton(
                    text = text,
                    height = Spacing.s48,
                    background = MaterialTheme.colors.primary,
                ) {}
            }
        }

        composeTestRule
            .onNodeWithTag(COMMON_BUTTON_BOX_ID)
            .assertIsDisplayed()
    }

    @Test
    fun checkTextIsDisplayedInCommonButtonTest() {
        val text = "Play"
        val height = Spacing.s48
        composeTestRule.setContent {
            PreviewContent {
                CommonButton(
                    text = text,
                    height = height,
                    background = MaterialTheme.colors.primary,
                ) {}
            }
        }

        composeTestRule
            .onNode(hasTestTag(COMMON_BUTTON_BOX_ID), true)
            .onChild()
            .assert(hasText(text))
    }

    @Test
    fun checkHeightInCommonButtonIsTheExpectedTest() {
        val text = "Play"
        val height = Spacing.s48
        composeTestRule.setContent {
            PreviewContent {
                CommonButton(
                    text = text,
                    height = height,
                    background = MaterialTheme.colors.primary,
                ) {}
            }
        }

        composeTestRule
            .onNode(hasTestTag(COMMON_BUTTON_BOX_ID), true)
            .assertHeightIsEqualTo(height)
    }

    @Test
    fun checkBackgroundInCommonButtonIsTheExpectedTest() {
        val text = "Play"
        val height = Spacing.s48
        val background = Orange
        composeTestRule.setContent {
            PreviewContent {
                CommonButton(
                    text = text,
                    height = height,
                    background = background,
                ) {}
            }
        }

        val result =
            composeTestRule
                .onNode(hasTestTag(COMMON_BUTTON_BOX_ID), true)
                .hasBackground(background)

        result shouldBe true
    }

    private fun SemanticsNodeInteraction.hasBackground(color: Color): Boolean =
        fetchSemanticsNode()
            .layoutInfo
            .getModifierInfo()
            .filter { modifierInfo ->
                modifierInfo.modifier == Modifier.background(color)
            }.size == 1
}
