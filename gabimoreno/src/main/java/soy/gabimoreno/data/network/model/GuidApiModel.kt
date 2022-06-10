package soy.gabimoreno.data.network.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "guid", strict = false)
class GuidApiModel @JvmOverloads constructor(
    @field: Element(name = "_isPermaLink", required = false)
    var isPermaLink: Boolean = false,

    @field: Element(name = "__text", required = false)
    var text: String = ""
)
