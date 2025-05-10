package soy.gabimoreno.data.remote.mapper

import soy.gabimoreno.data.remote.model.JwtAuthApiModel
import soy.gabimoreno.domain.model.login.JwtAuth

fun JwtAuthApiModel.toDomain() = JwtAuth(
    token = token,
    userEmail = userEmail,
    userNiceName = userNiceName,
    userDisplayName = userDisplayName,
)
