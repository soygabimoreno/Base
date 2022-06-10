package soy.gabimoreno.data.network.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "image", strict = false)
class ImageApiModel @JvmOverloads constructor(
    @field: Element(name = "_href", required = false)
    var href: String = "",

    @field: Element(name = "__prefix", required = false)
    var prefix: String = ""
)
