buildscript {
    extra.apply {
        set("version_kotlin", "1.6.10")
        set("version_android_gradle_plugin", "7.1.3")
        set("version_google_services", "4.3.10")
        set("version_compose", "1.1.1")
        set("version_compose_nav", "2.4.2")
        set("version_hilt", "2.41")
        set("version_hilt_navigation_compose", "1.0.0")
        set("version_detekt", "1.19.0")
        set("version_coroutines", "1.6.1")
        set("version_exo_player", "2.17.1")
    }

    dependencies {
        classpath("com.google.gms:google-services:${rootProject.extra["version_google_services"]}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${rootProject.extra["version_hilt"]}")
    }
}

plugins {
    id("com.android.application") version "${extra["version_android_gradle_plugin"]}" apply false
    id("com.android.library") version "${extra["version_android_gradle_plugin"]}" apply false
    id("org.jetbrains.kotlin.android") version "${extra["version_kotlin"]}" apply false
    id("org.jetbrains.kotlin.jvm") version "${extra["version_kotlin"]}" apply false
    id("io.gitlab.arturbosch.detekt") version "${extra["version_detekt"]}"
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
