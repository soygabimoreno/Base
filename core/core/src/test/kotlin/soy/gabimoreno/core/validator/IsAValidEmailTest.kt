package soy.gabimoreno.core.validator

import org.amshove.kluent.shouldBe
import org.junit.Test

class IsAValidEmailTest {
    @Test
    fun `GIVEN a standard email WHEN isAValidEmail THEN true`() {
        val result = isAValidEmail("john.doe@example.com")

        result shouldBe true
    }

    @Test
    fun `GIVEN an email with plus alias WHEN isAValidEmail THEN true`() {
        val result = isAValidEmail("john+alias@example.com")

        result shouldBe true
    }

    @Test
    fun `GIVEN an email with underscore in local part WHEN isAValidEmail THEN true`() {
        val result = isAValidEmail("john_doe@example.com")

        result shouldBe true
    }

    @Test
    fun `GIVEN an email with percent in local part WHEN isAValidEmail THEN true`() {
        val result = isAValidEmail("john%doe@example.com")

        result shouldBe true
    }

    @Test
    fun `GIVEN an email with subdomain WHEN isAValidEmail THEN true`() {
        val result = isAValidEmail("john.doe@mail.example.com")

        result shouldBe true
    }

    @Test
    fun `GIVEN an email without at symbol WHEN isAValidEmail THEN false`() {
        val result = isAValidEmail("john.doeexample.com")

        result shouldBe false
    }

    @Test
    fun `GIVEN an email without local part WHEN isAValidEmail THEN false`() {
        val result = isAValidEmail("@example.com")

        result shouldBe false
    }

    @Test
    fun `GIVEN an email without domain WHEN isAValidEmail THEN false`() {
        val result = isAValidEmail("john.doe@")

        result shouldBe false
    }

    @Test
    fun `GIVEN an email without top level domain WHEN isAValidEmail THEN false`() {
        val result = isAValidEmail("john.doe@example")

        result shouldBe false
    }

    @Test
    fun `GIVEN an email with double at symbol WHEN isAValidEmail THEN false`() {
        val result = isAValidEmail("john@@example.com")

        result shouldBe false
    }

    @Test
    fun `GIVEN an email with spaces WHEN isAValidEmail THEN false`() {
        val result = isAValidEmail("john doe@example.com")

        result shouldBe false
    }

    @Test
    fun `GIVEN an empty email WHEN isAValidEmail THEN false`() {
        val result = isAValidEmail("")

        result shouldBe false
    }
}
