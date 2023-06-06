plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

@Suppress("UnstableApiUsage")
android {
    compileSdk = 32

    defaultConfig {
        minSdk = 23
        targetSdk = 32

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")

    implementation("com.google.dagger:hilt-android:${rootProject.extra["version_hilt"]}")
    kapt("com.google.dagger:hilt-android-compiler:${rootProject.extra["version_hilt"]}")

    implementation("com.google.firebase:firebase-config-ktx:21.1.1")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.amshove.kluent:kluent-android:1.68")
    testImplementation("io.mockk:mockk:1.12.4")
}
