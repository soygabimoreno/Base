package soy.gabimoreno.domain.model.login

data class JwtAuth(
    val token: String,
    val userEmail: String,
    val userNiceName: String,
    val userDisplayName: String,
)
