package soy.gabimoreno.data.network.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "enclosure", strict = false)
class EnclosureApiModel @JvmOverloads constructor(
    @field: Element(name = "_url", required = false)
    var url: String = "",

    @field: Element(name = "_length", required = false)
    var length: Long = 0L,

    @field: Element(name = "_type", required = false)
    var type: String = ""
)
