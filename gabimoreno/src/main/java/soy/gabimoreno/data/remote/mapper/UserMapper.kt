package soy.gabimoreno.data.remote.mapper

import soy.gabimoreno.data.remote.model.UserApiModel
import soy.gabimoreno.domain.model.login.User

fun UserApiModel.toDomain() =
    User(
        id = id,
        username = username,
        niceName = niceName,
        email = email,
        url = url,
        registered = registered,
        displayName = displayName,
        firstName = firstName,
        lastName = lastName,
        nickname = nickname,
        description = description,
        capabilities = capabilities,
        avatar = avatar,
    )
