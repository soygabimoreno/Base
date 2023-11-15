buildscript {
    extra.apply {
        set("version_compose_compiler", "1.4.7")
        set("version_hilt", "2.46.1")
        set("version_detekt", "1.19.0")
        set("version_detekt", "1.19.0")
    }

    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:${rootProject.extra["version_hilt"]}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.7")
    }
}

plugins {
    kotlin("multiplatform").apply(false)
    id("com.android.application").apply(false)
    id("com.android.library").apply(false)
    id("org.jetbrains.compose").apply(false)
//    id("org.jetbrains.kotlin.jvm") version "${extra["version_kotlin"]}" apply false
    id("io.gitlab.arturbosch.detekt") version "${extra["version_detekt"]}"
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}

apply(from = "gradle-scripts/detekt.gradle")
apply(from = "gradle-scripts/ktlint.gradle.kts")

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
