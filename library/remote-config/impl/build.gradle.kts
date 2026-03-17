import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("soy.gabimoreno.android.library")
    alias(libs.plugins.ksp)
}

android {
    namespace = "soy.gabimoreno.remoteconfig.impl"
    val localProperties = gradleLocalProperties(rootDir, providers)
    val masterKey = localProperties.getProperty("MASTER_KEY") ?: error("MASTER_KEY not found")

    defaultConfig {
        buildConfigField("String", "MASTER_KEY", "\"$masterKey\"")
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

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.library.remoteConfig.api)

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
