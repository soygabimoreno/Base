package soy.gabimoreno.fake

import soy.gabimoreno.domain.model.login.User

fun buildUser() = User(
    id = 1,
    username = "username",
    niceName = "niceName",
    email = "email",
    url = "url",
    registered = "registered",
    displayName = "displayName",
    firstName = "firstName",
    lastName = "lastName",
    nickname = "nickname",
    description = "description",
    capabilities = "capabilities",
    avatar = "avatar"
)
