package soy.gabimoreno.data.network.mapper

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import soy.gabimoreno.data.network.model.Category

class ToSubCategoryTitleResIdKtTest {

    @Test
    fun `GIVEN the happy path WHEN call THEN return null`() {
        val categoryIds = listOf(Category.PREMIUM.id, Category.PREMIUM_AUDIO_COURSES.id)

        val result = categoryIds.toSubcategoryTitleResId()

        result shouldBeEqualTo Category.PREMIUM_AUDIO_COURSES.title
    }

    @Test
    fun `GIVEN an empty categoryIds WHEN call THEN return null`() {
        val categoryIds = emptyList<Int>()

        val result = categoryIds.toSubcategoryTitleResId()

        result shouldBe null
    }

    @Test
    fun `GIVEN only 1 category WHEN call THEN return null`() {
        val categoryIds = listOf(Category.PREMIUM.id)

        val result = categoryIds.toSubcategoryTitleResId()

        result shouldBe null
    }

    @Test
    fun `GIVEN more than 2 categories WHEN call THEN return null`() {
        val categoryIds = listOf(
            Category.PREMIUM.id,
            Category.PREMIUM_AUDIO_COURSES.id,
            Category.PREMIUM_ALGORITHMS.id
        )

        val result = categoryIds.toSubcategoryTitleResId()

        result shouldBe null
    }
}
