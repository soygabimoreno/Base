buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:${libs.versions.hilt.android.get()}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:${libs.versions.crashlytics.get()}")
    }
}

plugins {
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.jetbrainsCompose).apply(false)
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.detekt.gradle.plugin)
    alias(libs.plugins.secrets.gradle.plugin) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
}

apply(from = "gradle-scripts/detekt.gradle")
apply(from = "gradle-scripts/ktlint.gradle.kts")
