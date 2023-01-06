package soy.gabimoreno.data.remote.mapper

import org.amshove.kluent.shouldBe
import org.junit.Test
import soy.gabimoreno.data.remote.model.AuthCookieApiModel
import soy.gabimoreno.data.remote.model.UserApiModel

class AuthCookieMapperKtTest {

    @Test
    fun `GIVEN an AuthCookieApiModel WHEN toDomain THEN return the expected AuthCookie`() {
        val authCookieApiModel = buildAuthCookieApiModel()

        val authCookie = authCookieApiModel.toDomain()

        authCookie.status shouldBe STATUS
        authCookie.cookie shouldBe COOKIE
        authCookie.cookieAdmin shouldBe COOKIE_ADMIN
        authCookie.cookieName shouldBe COOKIE_NAME

        val user = authCookie.user
        user.id shouldBe ID
        user.username shouldBe USERNAME
        user.niceName shouldBe NICE_NAME
        user.email shouldBe EMAIL
        user.url shouldBe URL
        user.registered shouldBe REGISTERED
        user.displayName shouldBe DISPLAY_NAME
        user.firstName shouldBe FIRST_NAME
        user.lastName shouldBe LAST_NAME
        user.nickname shouldBe NICKNAME
        user.description shouldBe DESCRIPTION
        user.capabilities shouldBe CAPABILITIES
        user.avatar shouldBe AVATAR
    }
}

private fun buildAuthCookieApiModel() = AuthCookieApiModel(
    status = STATUS,
    cookie = COOKIE,
    cookieAdmin = COOKIE_ADMIN,
    cookieName = COOKIE_NAME,
    userApiModel = buildUserApiModel()
)

private fun buildUserApiModel() = UserApiModel(
    id = ID,
    username = USERNAME,
    niceName = NICE_NAME,
    email = EMAIL,
    url = URL,
    registered = REGISTERED,
    displayName = DISPLAY_NAME,
    firstName = FIRST_NAME,
    lastName = LAST_NAME,
    nickname = NICKNAME,
    description = DESCRIPTION,
    capabilities = CAPABILITIES,
    avatar = AVATAR
)

private const val STATUS = "status"
private const val COOKIE = "cookie"
private const val COOKIE_ADMIN = "cookieAdmin"
private const val COOKIE_NAME = "cookieName"

private const val ID = 1
private const val USERNAME = "username"
private const val NICE_NAME = "niceName"
private const val EMAIL = "email"
private const val URL = "url"
private const val REGISTERED = "registered"
private const val DISPLAY_NAME = "displayName"
private const val FIRST_NAME = "firstName"
private const val LAST_NAME = "lastName"
private const val NICKNAME = "nickname"
private const val DESCRIPTION = "description"
private const val CAPABILITIES = "capabilities"
private const val AVATAR = "avatar"
