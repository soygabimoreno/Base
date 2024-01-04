import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension

plugins {
    id("com.android.application")
    kotlin("android")
    alias(libs.plugins.google.services)
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.firebase.crashlytics")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("org.jetbrains.compose")
}

@Suppress("UnstableApiUsage")
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

    namespace = "soy.gabimoreno"
    compileSdk = extra["compileSdk.version"]?.toString()?.toInt()

    defaultConfig {
        applicationId = "soy.gabimoreno"
        minSdk = extra["minSdk.version"]?.toString()?.toInt()
        targetSdk = extra["targetSdk.version"]?.toString()?.toInt()
        versionCode = 51
        versionName = "1.2.8"

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
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (isLocalBuild()) {
                signingConfig = signingConfigs.getByName(name)
            }
            configure<CrashlyticsExtension> {
                mappingFileUploadEnabled = false
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
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
    implementation(project(":shared"))

    implementation(libs.core.ktx)
    implementation(libs.google.material)
    implementation(libs.compose.material)
    implementation(libs.material.icons.extended)
    implementation(libs.navigation.compose)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.compose.runtime)
    implementation(platform("com.google.firebase:firebase-bom:32.1.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.moshi)
    kapt(libs.moshi.codegen)
    implementation(libs.logging.interceptor)
    implementation(libs.rssparser)
    implementation(libs.exoplayer)
    implementation(libs.extension.mediasession)
    implementation(libs.glide)
    implementation(libs.accompanist.insets)
    implementation(libs.accompanist.coil)
    implementation(libs.arrow.core)
    implementation(libs.datastore.preferences)
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)
    implementation(libs.gson)
    implementation(libs.clarity)

    testImplementation(libs.junit)
    testImplementation(libs.kluent)
    testImplementation(libs.mockk)
    testImplementation(libs.robolectric)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)

    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.compose.ui.test.junit4)
    androidTestImplementation(libs.kluent.android)
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)
    androidTestImplementation(libs.mockwebserver)
    androidTestImplementation(libs.okhttp3.idling.resource)
    androidTestImplementation(libs.kotlinx.coroutines.test)

    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
}

fun isLocalBuild(): Boolean {
    return System.getenv("CI") == null
}
