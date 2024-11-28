plugins {
    alias(libs.plugins.xbot.android.library)
}

android {
    namespace = "com.xbot.common"
}

dependencies {
    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext)
}