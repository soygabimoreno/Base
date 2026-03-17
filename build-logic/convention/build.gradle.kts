plugins {
    `kotlin-dsl`
}

group = "soy.gabimoreno.convention"

dependencies {
    implementation(libs.android.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.compose.gradle.plugin)
    implementation(libs.detekt.gradle.plugin)
    implementation(libs.ktlint.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "soy.gabimoreno.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
    }
}

tasks.validatePlugins {
    enableStricterValidation = true
}

kotlinDslPluginOptions {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "21"
        }
    }
}
