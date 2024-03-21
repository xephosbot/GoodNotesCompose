plugins {
    alias(libs.plugins.xbot.android.library)
    alias(libs.plugins.xbot.android.hilt)
    alias(libs.plugins.xbot.android.room)
}
android {
    namespace = "com.xbot.data"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    // Project-level dependencies
    implementation(project(":domain"))

    // AndroidX dependencies
    implementation(libs.androidx.dataStore)

    // Kotlin dependencies
    implementation(libs.kotlinx.datetime)

    // Testing dependencies
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.ext)
}