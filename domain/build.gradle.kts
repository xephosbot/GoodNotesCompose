plugins {
    alias(libs.plugins.xbot.android.library)
    alias(libs.plugins.xbot.android.hilt)
}

android {
    namespace = "com.xbot.domain"
}

dependencies {
    // Testing dependencies
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.ext)
}