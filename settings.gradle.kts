pluginManagement {
    includeBuild("build-logic")

    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Fetch-Rewards"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")

// Core
include(":core:database")
include(":core:designsystem")
include(":core:network")
include(":core:sync:engine")
include(":core:sync:common")
include(":core:ui")

// Data
include(":data:items")

// Feature
include(":feature:items")

include(":core:util")
