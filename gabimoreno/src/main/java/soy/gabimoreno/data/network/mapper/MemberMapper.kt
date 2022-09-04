package soy.gabimoreno.data.network.mapper

import soy.gabimoreno.data.network.model.MemberApiModel
import soy.gabimoreno.domain.model.login.Member

fun List<MemberApiModel>.toDomain(): Member {
    return this[0].toDomain()
}

fun MemberApiModel.toDomain() = Member(
    isActive = isActive
)
