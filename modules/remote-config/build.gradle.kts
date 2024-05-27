plugins {
    alias(libs.plugins.androidLibrary)
    kotlin("android")
    kotlin("kapt")
}

@Suppress("UnstableApiUsage")
android {
    namespace = "soy.gabimoreno.remoteconfig"
    compileSdk = libs.versions.sdk.compile.get().toInt()

    defaultConfig {
        minSdk = libs.versions.sdk.minimum.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.google.material)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation("com.google.firebase:firebase-config-ktx:21.1.1")

    testImplementation(libs.junit)
    testImplementation(libs.kluent.android)
    testImplementation(libs.mockk)
}
