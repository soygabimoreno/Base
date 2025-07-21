plugins {
    alias(libs.plugins.androidLibrary)
    kotlin("android")
}

android {
    namespace = "soy.gabimoreno.core.testing"
    compileSdk =
        libs.versions.sdk.compile
            .get()
            .toInt()

    defaultConfig {
        minSdk =
            libs.versions.sdk.minimum
                .get()
                .toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_21.toString()
    }
}

dependencies {
    testImplementation(libs.junit)
    implementation(libs.mockk)
    implementation(libs.kotest)
}
