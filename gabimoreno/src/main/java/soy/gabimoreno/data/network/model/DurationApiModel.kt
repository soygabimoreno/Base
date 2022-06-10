package soy.gabimoreno.data.network.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "duration", strict = false)
class DurationApiModel @JvmOverloads constructor(
    @field: Element(name = "__prefix", required = false)
    var prefix: String = "",

    @field: Element(name = "__text", required = false)
    var text: Long = 0L
)
