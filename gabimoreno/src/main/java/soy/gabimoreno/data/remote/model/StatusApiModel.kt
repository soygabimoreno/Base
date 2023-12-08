package soy.gabimoreno.data.remote.model

import com.squareup.moshi.Json

enum class StatusApiModel {
    @Json(name = "active")
    ACTIVE,

    @Json(name = "expired")
    EXPIRED,

    @Json(name = "canceled")
    CANCELED,

    @Json(name = "pending")
    PENDING,

    @Json(name = "free")
    FREE,
}
