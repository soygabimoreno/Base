package soy.gabimoreno.data.network.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "explicit", strict = false)
class ExplicitApiModel @JvmOverloads constructor(
    @field: Element(name = "__prefix", required = false)
    var prefix: String = "",

    @field: Element(name = "__text", required = false)
    var text: String = ""
)
