plugins {
    alias(libs.plugins.fetchrewards.android.feature)
    alias(libs.plugins.fetchrewards.android.library.compose)
}

android {
    namespace = "com.maxkohne.fetchrewards.feature.list"
}

dependencies {
    implementation(projects.core.sync.common)
    implementation(projects.core.util)
    implementation(projects.data.items)
}