package soy.gabimoreno.domain.session

interface MemberSession {
    // TODO: This is when there will be a difference between a user logged in and a user premium or active
    // TODO: Now, only active is taken into account
//    fun isLoggedIn(): Boolean
//    fun setLoggedIn(isLoggedIn: Boolean)
//    fun isPremium(): Boolean
//    fun setPremium(isPremium: Boolean)
    suspend fun isActive(): Boolean
    suspend fun setActive(isActive: Boolean)
}
