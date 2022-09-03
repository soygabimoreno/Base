package soy.gabimoreno.data.network.service

import retrofit2.http.GET
import retrofit2.http.Query
import soy.gabimoreno.data.network.model.AuthCookieApiModel

interface GabiMorenoService {

    @GET("api/user/generate_auth_cookie/")
    suspend fun generateAuthCookie(
        @Query("email") email: String,
        @Query("password") password: String
    ): AuthCookieApiModel
}
