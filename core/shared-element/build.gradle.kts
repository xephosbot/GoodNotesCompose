plugins {
    alias(libs.plugins.xbot.android.library)
    alias(libs.plugins.xbot.android.library.compose)
}

android {
    namespace = "com.xbot.sharedelement"
}

dependencies {
    // Compose dependencies
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)

    // Testing dependencies
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.ext)
}