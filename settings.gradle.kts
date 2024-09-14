pluginManagement {
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

rootProject.name = "WatherApplication"
include(":app")
include(":feature")
include(":data")
include(":di")
include(":domain")
include(":navigation")
include(":feature:splash")
include(":feature:home")
include(":feature:favorites")
include(":feature:forecast")
