package soy.gabimoreno.data.remote.model

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class CategoryKtTest {
    @Test
    fun `GIVEN a category WHEN toQueryValue THEN get the expected result`() {
        val list = listOf(Category.PREMIUM)

        val result = list.toQueryValue()

        result shouldBeEqualTo Category.PREMIUM.id.toString()
    }

    @Test
    fun `GIVEN more than one category WHEN toQueryValue THEN get the expected result`() {
        val list = listOf(Category.PREMIUM, Category.PREMIUM_ALGORITHMS)

        val result = list.toQueryValue()

        val premiumId = Category.PREMIUM.id
        val premiumAlgorithmId = Category.PREMIUM_ALGORITHMS.id
        result shouldBeEqualTo "$premiumId,$premiumAlgorithmId"
    }
}
