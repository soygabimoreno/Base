package soy.gabimoreno.domain.model.login

data class User(
    val id: Int,
    val username: String,
    val niceName: String,
    val email: String,
    val url: String,
    val registered: String,
    val displayName: String,
    val firstName: String,
    val lastName: String,
    val nickname: String,
    val description: String,
    val capabilities: String,
    val avatar: String?,
)
