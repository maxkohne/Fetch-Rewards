plugins {
    alias(libs.plugins.fetchrewards.android.library)
}

android {
    namespace = "com.maxkohne.fetchrewards.core.network"
}

dependencies {
    api(libs.retrofit.core)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.okhttp.logging)
}