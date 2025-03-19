import androidx.room.gradle.RoomExtension
import com.google.devtools.ksp.gradle.KspExtension
import com.maxkohne.fetchrewards.buildlogic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

internal class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Plugins
            apply(plugin = "androidx.room")
            apply(plugin = "com.google.devtools.ksp")

            // Extensions
            extensions.configure<KspExtension> {
                arg("room.generateKotlin", "true")
            }
            extensions.configure<RoomExtension> {
                // The schemas directory contains a schema file for each version of the Room database.
                // This is required to enable Room auto migrations.
                // See https://developer.android.com/reference/kotlin/androidx/room/AutoMigration.
                schemaDirectory("$projectDir/schemas")
            }

            // Dependencies
            dependencies {
                "implementation"(libs.findLibrary("androidx-room.runtime").get())
                "implementation"(libs.findLibrary("androidx-room.ktx").get())
                "ksp"(libs.findLibrary("androidx-room.compiler").get())
            }
        }
    }
}