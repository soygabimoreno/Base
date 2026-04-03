plugins {
    id("soy.gabimoreno.android.library")
}

android {
    namespace = "soy.gabimoreno.auth"
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.google.material)

    testImplementation(libs.junit)
    testImplementation(libs.kluent.android)

    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
}
