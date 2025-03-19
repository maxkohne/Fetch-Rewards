import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import com.maxkohne.fetchrewards.buildlogic.convention.TARGET_SDK
import com.maxkohne.fetchrewards.buildlogic.convention.configureKotlinAndroid
import com.maxkohne.fetchrewards.buildlogic.convention.disableUnnecessaryAndroidTests
import com.maxkohne.fetchrewards.buildlogic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

internal class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Plugins
            apply(plugin = "com.android.library")
            apply(plugin = "org.jetbrains.kotlin.android")
            apply(plugin = "kotlin-parcelize")
            apply(plugin = "fetchrewards.hilt")
            apply(plugin = "fetchrewards.serialization")

            // Extensions
            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = TARGET_SDK
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                testOptions.animationsDisabled = true
                // The resource prefix is derived from the module name,
                // so resources inside ":core:module1" must be prefixed with "core_module1_"
//                resourcePrefix = path.split("""\W""".toRegex()).drop(1).distinct().joinToString(separator = "_").lowercase() + "_"
            }
            extensions.configure<LibraryAndroidComponentsExtension> {
                disableUnnecessaryAndroidTests(target)
            }

            // Dependencies
            dependencies {
                "implementation"(libs.findLibrary("kotlinx.coroutines.core").get())
                "implementation"(libs.findLibrary("kotlinx.coroutines.android").get())

                // Unit Testing
                "testImplementation"(libs.findLibrary("androidx.test.core").get())
                "testImplementation"(libs.findLibrary("androidx.test.espresso.core").get())
                "testImplementation"(libs.findLibrary("androidx.test.junit").get())
                "testImplementation"(libs.findLibrary("androidx.test.rules").get())
                "testImplementation"(libs.findLibrary("androidx.test.runner").get())
                "testImplementation"(libs.findLibrary("androidx.test.uiautomator").get())
                "testImplementation"(libs.findLibrary("junit").get())
                "testImplementation"(libs.findLibrary("kotlinx.coroutines.test").get())
                "testImplementation"(libs.findLibrary("mockito-kotlin").get())
                "testImplementation"(libs.findLibrary("truth").get())
                "testImplementation"(libs.findLibrary("turbine").get())
                "testImplementation"(libs.findLibrary("robolectric").get())
                "testImplementation"(kotlin("test"))

                // Android Testing
                "androidTestImplementation"(libs.findLibrary("androidx.test.core").get())
                "androidTestImplementation"(libs.findLibrary("androidx.test.espresso.core").get())
                "androidTestImplementation"(libs.findLibrary("androidx.test.junit").get())
                "androidTestImplementation"(libs.findLibrary("androidx.test.rules").get())
                "androidTestImplementation"(libs.findLibrary("androidx.test.runner").get())
                "androidTestImplementation"(libs.findLibrary("androidx.test.uiautomator").get())
                "androidTestImplementation"(libs.findLibrary("kotlinx.coroutines.test").get())
                "androidTestImplementation"(libs.findLibrary("mockito-kotlin").get())
                "androidTestImplementation"(libs.findLibrary("truth").get())
                "androidTestImplementation"(libs.findLibrary("turbine").get())
                "androidTestImplementation"(libs.findLibrary("robolectric").get())
                "androidTestImplementation"(kotlin("test"))
            }
        }
    }
}
