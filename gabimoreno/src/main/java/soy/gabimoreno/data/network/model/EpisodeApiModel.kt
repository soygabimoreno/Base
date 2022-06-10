package soy.gabimoreno.data.network.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

@Root(name = "episode", strict = false)
@Namespace(reference = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
class EpisodeApiModel @JvmOverloads constructor(
    @field: Element(name = "prefix", required = false)
    var prefix: String = "",

    @field: Element(name = "text", required = false)
    var text: String = ""
)
