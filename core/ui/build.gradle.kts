plugins {
    alias(libs.plugins.xbot.android.library)
    alias(libs.plugins.xbot.android.library.compose)
}

android {
    namespace = "com.xbot.ui"
}

dependencies {
    // Compose dependencies
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.ui.tooling.preview)

    // Material dependencies
    implementation(libs.material)

    // Testing dependencies
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.ext)
}