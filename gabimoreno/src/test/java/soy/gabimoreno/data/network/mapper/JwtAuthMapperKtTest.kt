package soy.gabimoreno.data.network.mapper

import org.amshove.kluent.shouldBe
import org.junit.Test
import soy.gabimoreno.data.network.model.JwtAuthApiModel

class JwtAuthMapperKtTest {

    @Test
    fun `GIVEN an JwtAuthApiModel WHEN toDomain THEN return the expected JwtAuth`() {
        val jwtAuthApiModel = buildJwtAuthApiModel()

        val result = jwtAuthApiModel.toDomain()

        result.token shouldBe TOKEN
        result.userEmail shouldBe USER_EMAIL
        result.userNiceName shouldBe USER_NICE_NAME
        result.userDisplayName shouldBe USER_DISPLAY_NAME
    }
}

private fun buildJwtAuthApiModel() = JwtAuthApiModel(
    token = TOKEN,
    userEmail = USER_EMAIL,
    userNiceName = USER_NICE_NAME,
    userDisplayName = USER_DISPLAY_NAME
)

private const val TOKEN = "token"
private const val USER_EMAIL = "userEmail"
private const val USER_NICE_NAME = "userNiceName"
private const val USER_DISPLAY_NAME = "userDisplayName"
