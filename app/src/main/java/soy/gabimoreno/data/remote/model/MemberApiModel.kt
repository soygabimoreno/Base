package soy.gabimoreno.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MemberApiModel(
    @Json(name = "status")
    val statusApiModel: StatusApiModel,
    @Json(name = "is_active")
    val isActive: Boolean,
)
