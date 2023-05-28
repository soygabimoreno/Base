import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.firebase.crashlytics")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    if (isLocalBuild()) {
        signingConfigs {
            create("release") {
                val localProperties = gradleLocalProperties(rootDir)
                keyAlias = localProperties.getProperty("keystore.keyAlias")
                storeFile = file(localProperties.getProperty("keystore.storeFile") ?: "fakePath")
                storePassword = localProperties.getProperty("keystore.storePassword")
                keyPassword = localProperties.getProperty("keystore.keyPassword")
            }
        }
    }

    compileSdk = 33

    defaultConfig {
        applicationId = "soy.gabimoreno"
        minSdk = 23
        targetSdk = 33
        versionCode = 45
        versionName = "1.2.2"

        testInstrumentationRunner = "soy.gabimoreno.di.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    bundle {
        density { enableSplit = true }
        abi { enableSplit = true }
        language { enableSplit = false }
    }

    buildTypes {
        debug {
            versionNameSuffix = "-DEBUG"
            applicationIdSuffix = ".debug"
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            isCrunchPngs = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
            extra["enableCrashlytics"] = false
            extra["alwaysUpdateBuildId"] = false
            splits {
                abi.isEnable = false
                density.isEnable = false
            }
            configure<CrashlyticsExtension> {
                mappingFileUploadEnabled = false
            }
        }
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (isLocalBuild()) {
                signingConfig = signingConfigs.getByName(name)
            }
            configure<CrashlyticsExtension> {
                mappingFileUploadEnabled = true
            }
        }
    }

    applicationVariants.forEach { variant ->
        if (variant.buildType.name == "debug") {
            variant.mergedFlavor.resourceConfigurations.clear()
            variant.mergedFlavor.resourceConfigurations.add("es")
            variant.mergedFlavor.resourceConfigurations.add("xhdpi")
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
    implementation(project(":modules:core"))
    implementation(project(":modules:core-testing"))
    implementation(project(":modules:core-view"))
    implementation(project(":modules:framework"))
    implementation(project(":modules:remote-config"))

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("com.google.android.material:material:1.7.0")

    implementation("androidx.compose.ui:ui:${rootProject.extra["version_compose"]}")
    implementation("androidx.compose.material:material:${rootProject.extra["version_compose"]}")
    implementation("androidx.compose.material:material-icons-extended:${rootProject.extra["version_compose"]}")
    implementation("androidx.compose.ui:ui-tooling-preview:${rootProject.extra["version_compose"]}")
    implementation("androidx.navigation:navigation-compose:2.6.0-alpha02")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.activity:activity-compose:1.6.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0-alpha02")

    implementation(platform("com.google.firebase:firebase-bom:29.2.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")

    implementation("com.google.dagger:hilt-android:${rootProject.extra["version_hilt"]}")
    kapt("com.google.dagger:hilt-android-compiler:${rootProject.extra["version_hilt"]}")
    implementation("androidx.hilt:hilt-navigation-compose:${rootProject.extra["version_hilt_navigation_compose"]}")

    implementation("com.squareup.retrofit2:retrofit:${rootProject.extra["version_retrofit"]}")
    implementation("com.squareup.retrofit2:converter-moshi:${rootProject.extra["version_retrofit"]}")

    implementation("com.squareup.moshi:moshi:1.13.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.13.0")

    implementation("com.squareup.okhttp3:logging-interceptor:4.9.2")

    implementation("com.prof18.rssparser:rssparser:4.0.2")

    implementation("com.google.android.exoplayer:exoplayer:${rootProject.extra["version_exo_player"]}")
    implementation("com.google.android.exoplayer:extension-mediasession:${rootProject.extra["version_exo_player"]}")

    implementation("com.github.bumptech.glide:glide:${rootProject.extra["version_glide"]}")

    implementation("com.google.accompanist:accompanist-insets:${rootProject.extra["version_accompanist"]}")
    implementation("com.google.accompanist:accompanist-coil:${rootProject.extra["version_accompanist"]}")

    implementation("io.arrow-kt:arrow-core:1.0.1")

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation("androidx.room:room-runtime:${rootProject.extra["version_room"]}")
    kapt("androidx.room:room-compiler:${rootProject.extra["version_room"]}")

    implementation("com.google.code.gson:gson:${rootProject.extra["version_gson"]}")

    implementation("com.microsoft.clarity:clarity:1.3.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.amshove.kluent:kluent:1.68")
    testImplementation("io.mockk:mockk:1.12.4")
    testImplementation("org.robolectric:robolectric:4.8.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.3")

    testImplementation("app.cash.turbine:turbine:0.12.3")

    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${rootProject.extra["version_compose"]}")
    androidTestImplementation("org.amshove.kluent:kluent-android:1.68")

    androidTestImplementation("com.google.dagger:hilt-android-testing:${rootProject.extra["version_hilt"]}")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:${rootProject.extra["version_hilt"]}")
    androidTestImplementation("com.squareup.okhttp3:mockwebserver:4.9.3")
    androidTestImplementation("com.jakewharton.espresso:okhttp3-idling-resource:1.0.0")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.3")

    debugImplementation("androidx.compose.ui:ui-tooling:${rootProject.extra["version_compose"]}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${rootProject.extra["version_compose"]}")
}

fun isLocalBuild(): Boolean {
    return System.getenv("CI") == null
}
