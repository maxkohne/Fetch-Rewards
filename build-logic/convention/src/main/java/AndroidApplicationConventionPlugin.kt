import com.android.build.api.dsl.ApplicationExtension
import com.maxkohne.fetchrewards.buildlogic.convention.TARGET_SDK
import com.maxkohne.fetchrewards.buildlogic.convention.configureKotlinAndroid
import com.maxkohne.fetchrewards.buildlogic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

internal class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Plugins
            apply(plugin = "com.android.application")
            apply(plugin = "org.jetbrains.kotlin.android")
            apply(plugin = "kotlin-parcelize")
            apply(plugin = "fetchrewards.hilt")
            apply(plugin = "fetchrewards.serialization")

            // Extensions
            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = TARGET_SDK
                testOptions.animationsDisabled = true
            }

            // Dependencies
            dependencies {
                "implementation"(project(":core:ui"))
                "implementation"(project(":core:designsystem"))
                "implementation"(libs.findLibrary("kotlinx.coroutines.core").get())
                "implementation"(libs.findLibrary("kotlinx.coroutines.android").get())
            }
        }
    }
}