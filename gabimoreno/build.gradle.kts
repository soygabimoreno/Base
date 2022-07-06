plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "soy.gabimoreno"
        minSdk = 23
        targetSdk = 32
        versionCode = 10
        versionName = "0.10.0"

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
//    implementation(project(":modules:player")) // TODO: Add when moved all player files to this module
    implementation(project(":modules:framework"))
    implementation(project(":modules:core"))
    implementation(project(":modules:core-testing"))

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("com.google.android.material:material:1.3.0")

    implementation("androidx.compose.ui:ui:${rootProject.extra["version_compose"]}")
    implementation("androidx.compose.material:material:${rootProject.extra["version_compose"]}")
    implementation("androidx.compose.ui:ui-tooling-preview:${rootProject.extra["version_compose"]}")
    implementation("androidx.navigation:navigation-compose:2.4.0-alpha03")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.activity:activity-compose:1.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07")

    implementation(platform("com.google.firebase:firebase-bom:29.2.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")

    implementation("com.google.dagger:hilt-android:${rootProject.extra["version_hilt"]}")
    kapt("com.google.dagger:hilt-android-compiler:${rootProject.extra["version_hilt"]}")

    implementation("com.squareup.retrofit2:retrofit:${rootProject.extra["version_retrofit"]}")
    implementation("com.squareup.retrofit2:converter-gson:${rootProject.extra["version_retrofit"]}")

    implementation("com.prof18.rssparser:rssparser:4.0.2")

    implementation("com.google.android.exoplayer:exoplayer:${rootProject.extra["version_exo_player"]}")
    implementation("com.google.android.exoplayer:extension-mediasession:${rootProject.extra["version_exo_player"]}")

    implementation("com.github.bumptech.glide:glide:${rootProject.extra["version_glide"]}")

    implementation("com.google.accompanist:accompanist-insets:${rootProject.extra["version_accompanist"]}")
    implementation("com.google.accompanist:accompanist-coil:${rootProject.extra["version_accompanist"]}")

    implementation("androidx.palette:palette-ktx:1.0.0")

    implementation("io.arrow-kt:arrow-core:1.0.1")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.amshove.kluent:kluent-android:1.68")
    testImplementation("io.mockk:mockk:1.12.4")
    testImplementation("org.robolectric:robolectric:4.8.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.3")

    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${rootProject.extra["version_compose"]}")

    debugImplementation("androidx.compose.ui:ui-tooling:${rootProject.extra["version_compose"]}")
}
