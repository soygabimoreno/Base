package soy.gabimoreno.data.remote.mapper

import soy.gabimoreno.data.remote.model.StatusApiModel
import soy.gabimoreno.domain.model.login.Status

fun StatusApiModel.toDomain(): Status {
    return Status.valueOf(this.name.uppercase())
}
