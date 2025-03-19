plugins {
    alias(libs.plugins.fetchrewards.android.library)
}

android {
    namespace = "com.maxkohne.fetchrewards.data"
}

dependencies {
    implementation(projects.core.network)
    implementation(projects.core.database)
    implementation(projects.core.sync.common)
    implementation(projects.core.sync.engine)
}