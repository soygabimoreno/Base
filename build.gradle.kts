buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:${libs.versions.hilt.android.get()}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:${libs.versions.crashlytics.get()}")
    }
}

plugins {
    kotlin("multiplatform").apply(false)
    id("com.android.application").apply(false)
    id("com.android.library").apply(false)
    id("org.jetbrains.compose").apply(false)
    id("io.gitlab.arturbosch.detekt") version libs.versions.detekt.get()
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version libs.versions.secrets.gradle.plugin.get() apply false
}

apply(from = "gradle-scripts/detekt.gradle")
apply(from = "gradle-scripts/ktlint.gradle.kts")

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
