import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

buildscript {
    dependencies {
        classpath(
            "com.google.dagger:hilt-android-gradle-plugin:${libs.versions.hilt.android.get()}",
        )
        classpath(
            "com.google.firebase:firebase-crashlytics-gradle:${libs.versions.crashlytics.get()}",
        )
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
    alias(libs.plugins.ktlint) apply true
    alias(libs.plugins.room) apply false
}

ktlint {
    android = true
    reporters {
        reporter(ReporterType.CHECKSTYLE)
    }
}
subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}

apply(from = "gradle-scripts/detekt.gradle")
