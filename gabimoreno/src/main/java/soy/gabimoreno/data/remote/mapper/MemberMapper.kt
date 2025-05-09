package soy.gabimoreno.data.remote.mapper

import soy.gabimoreno.data.remote.model.MemberApiModel
import soy.gabimoreno.domain.model.login.Member

fun List<MemberApiModel>.toDomain(): Member {
    return this[0].toDomain()
}

fun MemberApiModel.toDomain() = Member(
    status = statusApiModel.toDomain(),
    isActive = isActive,
)
