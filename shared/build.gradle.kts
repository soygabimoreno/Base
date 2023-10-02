import java.util.Locale

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    val isMacOS = System.getProperty("os.name").lowercase(Locale.getDefault()).contains("mac")

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_17.toString()
            }
        }
    }

    if (isMacOS) {
        listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64()
        ).forEach {
            it.binaries.framework {
                baseName = "shared"
                isStatic = true
            }
        }
    }

    sourceSets {

        val androidMain by getting {
            dependencies {
                // TODO: Add dependencies here
            }
        }

        val androidUnitTest by getting {
            dependencies {
                // TODO: Add dependencies here
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        if (isMacOS) {
            val iosX64Main by getting {
                dependencies {
                    // TODO: Add dependencies here
                }
            }

            val iosArm64Main by getting {
                dependencies {
                    // TODO: Add dependencies here
                }
            }

            val iosSimulatorArm64Main by getting {
                dependencies {
                    // TODO: Add dependencies here
                }
            }
        }
    }
}

android {
    namespace = "soy.gabimoreno.shared"
    compileSdk = 33
    defaultConfig {
        minSdk = 23
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
