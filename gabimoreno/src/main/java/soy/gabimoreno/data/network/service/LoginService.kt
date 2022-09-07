package soy.gabimoreno.data.network.service

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import soy.gabimoreno.data.network.model.AuthCookieApiModel
import soy.gabimoreno.data.network.model.JwtAuthApiModel
import soy.gabimoreno.data.network.model.MemberApiModel

interface LoginService {

    @GET("api/user/generate_auth_cookie/")
    suspend fun generateAuthCookie(
        @Query("email") email: String,
        @Query("password") password: String,
    ): AuthCookieApiModel

    @POST("wp-json/jwt-auth/v1/token/")
    suspend fun obtainToken(
        @Query("username") username: String,
        @Query("password") password: String,
    ): JwtAuthApiModel

    @GET("wp-json/rcp/v1/members/")
    suspend fun getMembers(
        @Query("s") email: String,
    ): List<MemberApiModel>
}
