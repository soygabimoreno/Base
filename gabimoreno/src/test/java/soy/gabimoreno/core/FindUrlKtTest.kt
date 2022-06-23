package soy.gabimoreno.core

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class FindUrlKtTest {

    @Test
    fun `GIVEN text with url and a new line after it WHEN call THEN get the expected url`() {
        val textWithUrl = "foo $URL\nfoo"

        val result = textWithUrl.findUrl()

        result shouldBeEqualTo URL
    }

    @Test
    fun `GIVEN text with url and a space after it WHEN call THEN get the expected url`() {
        val textWithUrl = "foo $URL foo"

        val result = textWithUrl.findUrl()

        result shouldBeEqualTo URL
    }
}

private const val URL = "https://gabimoreno.soy"
