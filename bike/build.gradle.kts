plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "soy.gabimoreno.bike"
        minSdk = 23
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "${rootProject.extra["version_compose"]}"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":modules:framework"))

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.compose.ui:ui:${rootProject.extra["version_compose"]}")
    implementation("androidx.compose.material:material:${rootProject.extra["version_compose"]}")
    implementation("androidx.compose.ui:ui-tooling-preview:${rootProject.extra["version_compose"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("com.google.dagger:hilt-android:${rootProject.extra["version_hilt"]}")
    kapt("com.google.dagger:hilt-android-compiler:${rootProject.extra["version_hilt"]}")
    implementation("androidx.hilt:hilt-navigation-compose:${rootProject.extra["version_hilt_navigation_compose"]}")

    implementation("com.github.Jasonchenlijian:FastBle:2.4.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.amshove.kluent:kluent-android:1.68")
    testImplementation("io.mockk:mockk:1.12.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${rootProject.extra["version_compose"]}")

    debugImplementation("androidx.compose.ui:ui-tooling:${rootProject.extra["version_compose"]}")
}
