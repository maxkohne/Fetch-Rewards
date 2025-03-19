plugins {
    alias(libs.plugins.fetchrewards.android.library)
    alias(libs.plugins.fetchrewards.android.room)
}

android {
    namespace = "com.maxkohne.fetchrewards.core.database"
    packaging {
        resources.excludes.add("META-INF/*")
    }
}