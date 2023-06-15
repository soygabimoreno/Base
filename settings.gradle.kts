pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
rootProject.name = "Base"
include(":gabimoreno")
include(":shared")
include(":bike")

include(":modules:core")
include(":modules:core-testing")
include(":modules:core-view")
include(":modules:framework")
include(":modules:paparazzi")
include(":modules:player")
include(":modules:remote-config")
include(":modules:raffle")
