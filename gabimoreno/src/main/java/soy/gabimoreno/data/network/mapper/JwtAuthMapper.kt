package soy.gabimoreno.data.network.mapper

import soy.gabimoreno.data.network.model.JwtAuthApiModel
import soy.gabimoreno.domain.model.login.JwtAuth

fun JwtAuthApiModel.toDomain() = JwtAuth(
    token = token,
    userEmail = userEmail,
    userNiceName = userNiceName,
    userDisplayName = userDisplayName
)
