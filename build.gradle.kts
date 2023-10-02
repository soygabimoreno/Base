buildscript {
    extra.apply {
        set("version_kotlin", "1.8.21")
        set("version_android_gradle_plugin", "8.1.1")
        set("version_google_services", "4.3.15")
        set("version_compose", "1.4.3")
        set("version_compose_compiler", "1.4.7")
        set("version_compose_nav", "2.4.2")
        set("version_compose_activity", "1.7.2")
        set("version_hilt", "2.46.1")
        set("version_hilt_navigation_compose", "1.0.0")
        set("version_detekt", "1.19.0")
        set("version_coroutines", "1.6.1")
        set("version_exo_player", "2.15.0")
        set("version_glide", "4.12.0")
        set("version_retrofit", "2.9.0")
        set("version_accompanist", "0.12.0")
        set("version_detekt", "1.19.0")
        set("version_room", "2.5.1")
        set("version_gson", "2.9.0")
    }

    dependencies {
        classpath("com.google.gms:google-services:${rootProject.extra["version_google_services"]}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${rootProject.extra["version_hilt"]}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.7")
    }
}

plugins {
    kotlin("multiplatform").apply(false)
    id("com.android.application").apply(false)
    id("com.android.library").apply(false)
    id("org.jetbrains.compose").apply(false)
    id("org.jetbrains.kotlin.jvm") version "${extra["version_kotlin"]}" apply false
    id("io.gitlab.arturbosch.detekt") version "${extra["version_detekt"]}"
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}

apply(from = "gradle-scripts/detekt.gradle")
apply(from = "gradle-scripts/ktlint.gradle.kts")

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
