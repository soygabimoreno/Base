package soy.gabimoreno.core.validator

import org.amshove.kluent.shouldBe
import org.junit.Test

class IsAValidPasswordTest {
    @Test
    fun `GIVEN password with length major or equal to minimum WHEN isAValidPassword THEN returns true`() {
        val password = "1234"

        val result = isAValidPassword(password)

        result shouldBe true
    }

    @Test
    fun `GIVEN password with length minor than minimum WHEN isAValidPassword THEN returns false`() {
        val password = "123"

        val result = isAValidPassword(password)

        result shouldBe false
    }
}
