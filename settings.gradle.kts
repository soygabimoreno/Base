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
include(":bike")

include(":modules:core")
include(":modules:core-testing")
include(":modules:framework")
include(":modules:player")
include(":modules:remote-config")
