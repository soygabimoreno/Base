package soy.gabimoreno.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
