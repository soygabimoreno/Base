package soy.gabimoreno.bike.domain

import org.amshove.kluent.shouldBe
import org.junit.Test

class ToDecimalKtTest {

    @Test
    fun `GIVEN WHEN THEN `() {
        val bytes = byteArrayOf(101)

        val result = bytes.toDecimal()

        result shouldBe 5
    }
}
