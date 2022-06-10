package soy.gabimoreno.data.network.model

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "channel", strict = false)
class ChannelApiModel @JvmOverloads constructor(
    @field: ElementList(inline = true, required = false)
    var items: List<ItemApiModel>? = null
)
