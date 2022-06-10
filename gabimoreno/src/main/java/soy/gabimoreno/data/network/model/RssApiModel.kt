package soy.gabimoreno.data.network.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
class RssApiModel @JvmOverloads constructor(
    @field: Element(name = "channel")
    var channel: ChannelApiModel? = null
)
