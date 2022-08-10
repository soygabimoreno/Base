package soy.gabimoreno.core

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class NormalizeTextKtTest {

    @Test
    fun `GIVEN a normal text WHEN normalizeText THEN return the proper string`() {
        val givenText = "aeiou"
        val expectedText = "aeiou"

        val result = givenText.normalizeText()

        result shouldBeEqualTo expectedText
    }

    @Test
    fun `GIVEN a weird text WHEN normalizeText THEN return the proper string`() {
        val givenText = "áéíóúàèìòùäëïöüAEIOUñ"
        val expectedText = "aeiouaeiouaeiouaeioun"

        val result = givenText.normalizeText()

        result shouldBeEqualTo expectedText
    }
}
