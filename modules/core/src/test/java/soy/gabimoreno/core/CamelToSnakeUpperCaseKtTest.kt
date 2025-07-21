package soy.gabimoreno.core

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class CamelToSnakeUpperCaseKtTest {
    @Test
    fun `GIVEN a camelCase text WHEN call THEN get the expected text`() {
        val camelCaseText = "camelCaseText"

        val result = camelCaseText.camelToSnakeUpperCase()

        result shouldBeEqualTo "CAMEL_CASE_TEXT"
    }

    @Test
    fun `GIVEN an empty text WHEN call THEN get the expected text`() {
        val camelCaseText = ""

        val result = camelCaseText.camelToSnakeUpperCase()

        result shouldBeEqualTo ""
    }
}
