import java.util.Locale

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    val isMacOS = System.getProperty("os.name").lowercase(Locale.getDefault()).contains("mac")

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_1_8.toString()
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
            }
        }
    }

    sourceSets {

        val androidMain by getting {
            dependencies {

            }
        }

        val androidUnitTest by getting {
            dependencies {

            }
        }

        val commonMain by getting {
            dependencies {
                //put your multiplatform dependencies here
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
                    //put your multiplatform dependencies here
                }
            }

            val iosArm64Main by getting {
                dependencies {
                    //put your multiplatform dependencies here
                }
            }

            val iosSimulatorArm64Main by getting {
                dependencies {
                    //put your multiplatform dependencies here
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
}
