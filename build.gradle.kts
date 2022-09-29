buildscript {
    extra.apply {
        set("version_kotlin", "1.6.10")
        set("version_android_gradle_plugin", "7.3.0")
        set("version_google_services", "4.3.14")
        set("version_compose", "1.2.0-alpha03")
        set("version_compose_nav", "2.4.2")
        set("version_hilt", "2.41")
        set("version_hilt_navigation_compose", "1.0.0")
        set("version_detekt", "1.19.0")
        set("version_coroutines", "1.6.1")
        set("version_exo_player", "2.15.0")
        set("version_glide", "4.12.0")
        set("version_retrofit", "2.9.0")
        set("version_accompanist", "0.12.0")
        set("version_paparazzi", "1.0.0")
        set("detekt_version", "1.19.0")

        // This is just a comment to check the CI
    }

    dependencies {
        classpath("com.google.gms:google-services:${rootProject.extra["version_google_services"]}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${rootProject.extra["version_hilt"]}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.2")
        classpath("app.cash.paparazzi:paparazzi-gradle-plugin:${rootProject.extra["version_paparazzi"]}")
    }
}

plugins {
    id("com.android.application") version "${extra["version_android_gradle_plugin"]}" apply false
    id("com.android.library") version "${extra["version_android_gradle_plugin"]}" apply false
    id("org.jetbrains.kotlin.android") version "${extra["version_kotlin"]}" apply false
    id("org.jetbrains.kotlin.jvm") version "${extra["version_kotlin"]}" apply false
    id("io.gitlab.arturbosch.detekt") version "${extra["version_detekt"]}"
//    id("app.cash.paparazzi") version "${extra["version_paparazzi"]}"
}

apply(from = "gradle-scripts/detekt.gradle")
apply(from = "gradle-scripts/ktlint.gradle.kts")

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
