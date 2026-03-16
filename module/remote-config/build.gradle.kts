import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.androidLibrary)
    kotlin("android")
    alias(libs.plugins.ksp)
}

android {
    namespace = "soy.gabimoreno.remoteconfig"
    compileSdk =
        libs.versions.sdk.compile
            .get()
            .toInt()
    val localProperties = gradleLocalProperties(rootDir, providers)
    val masterKey = localProperties.getProperty("MASTER_KEY") ?: error("MASTER_KEY not found")

    defaultConfig {
        minSdk =
            libs.versions.sdk.minimum
                .get()
                .toInt()

        buildConfigField("String", "MASTER_KEY", "\"$masterKey\"")
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
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.google.material)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.config)

    testImplementation(libs.junit)
    testImplementation(libs.kluent.android)
    testImplementation(libs.mockk)
}
