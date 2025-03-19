import com.maxkohne.fetchrewards.buildlogic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

internal class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Plugins
            apply(plugin = "com.google.devtools.ksp")
            apply(plugin = "dagger.hilt.android.plugin")

            // Dependencies
            dependencies {
                "implementation"(libs.findLibrary("dagger.hilt.android").get())
                "ksp"(libs.findLibrary("dagger.hilt.compiler").get())
            }
        }
    }
}
