plugins {
    alias(libs.plugins.fetchrewards.android.library)
}

android {
    namespace = "com.maxkohne.fetchrewards.core.sync"
}

dependencies {
    implementation(projects.core.network)
    implementation(projects.core.sync.common)
}