import com.maxkohne.fetchrewards.buildlogic.convention.AppBuildType

plugins {
    alias(libs.plugins.fetchrewards.android.application)
    alias(libs.plugins.fetchrewards.android.application.compose)
}

android {
    namespace = "com.maxkohne.fetchrewards"

    defaultConfig {
        applicationId = "com.maxkohne.fetchrewards"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        debug {
            applicationIdSuffix = AppBuildType.DEBUG.applicationIdSuffix
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.feature.items)
    implementation(libs.androidx.navigation.compose)
}