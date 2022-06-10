package soy.gabimoreno.data.network.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "item", strict = false)
class ItemApiModel @JvmOverloads constructor(
    @field: Element(name = "title")
    var title: String = "",

    @field: Element(name = "description", required = false)
    var description: String = "",

    @field: Element(name = "link")
    var link: String = "",

    @field: Element(name = "guid")
    var guid: GuidApiModel? = null,

    @field: Element(name = "enclosure")
    var enclosure: EnclosureApiModel? = null,

    @field: Element(name = "image")
    var image: ImageApiModel? = null,

    @field: Element(name = "pubDate")
    var pubDate: String = "",

    @field: Element(name = "duration")
    var duration: DurationApiModel? = null,

    @field: Element(name = "explicit")
    var explicit: ExplicitApiModel? = null,

    @field: Element(name = "episode")
    var episode: Any? = null
)
