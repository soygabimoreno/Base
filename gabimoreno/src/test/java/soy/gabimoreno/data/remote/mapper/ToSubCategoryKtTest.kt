package soy.gabimoreno.data.remote.mapper

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import soy.gabimoreno.data.remote.mapper.category.toSubcategory
import soy.gabimoreno.data.remote.model.Category

class ToSubCategoryKtTest {

    @Test
    fun `GIVEN the happy path WHEN call THEN return null`() {
        val categoryIds = listOf(Category.PREMIUM.id, Category.PREMIUM_AUDIO_COURSES.id)

        val result = categoryIds.toSubcategory()

        result shouldBeEqualTo Category.PREMIUM_AUDIO_COURSES
    }

    @Test
    fun `GIVEN an empty categoryIds WHEN call THEN return null`() {
        val categoryIds = emptyList<Int>()

        val result = categoryIds.toSubcategory()

        result shouldBe null
    }

    @Test
    fun `GIVEN 1 PREMIUM category WHEN call THEN return the PREMIUM category`() {
        val categoryIds = listOf(Category.PREMIUM.id)

        val result = categoryIds.toSubcategory()

        result shouldBe Category.PREMIUM
    }

    @Test
    fun `GIVEN 1 subcategory WHEN call THEN return null`() {
        val categoryIds = listOf(Category.PREMIUM_NEWS.id)

        val result = categoryIds.toSubcategory()

        result shouldBe null
    }

    @Test
    fun `GIVEN 2 premium categories and another additional WHEN call THEN return the right one`() {
        val categoryIds = listOf(
            Category.PREMIUM.id,
            Category.PREMIUM_AUDIO_COURSES.id,
            151,
            12
        )

        val result = categoryIds.toSubcategory()

        result shouldBe Category.PREMIUM_AUDIO_COURSES
    }
}
