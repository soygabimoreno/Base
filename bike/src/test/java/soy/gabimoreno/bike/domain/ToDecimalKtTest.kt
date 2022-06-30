package soy.gabimoreno.bike.domain

import org.amshove.kluent.shouldBe
import org.junit.Test

class ToDecimalKtTest {

    @Test
    fun `GIVEN a ByteArray WHEN toDecimal THEN get the expected number`() {
        val bytes = byteArrayOf(101)

        val result = bytes.toDecimal()

        result shouldBe 5
    }
}
