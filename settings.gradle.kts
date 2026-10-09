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

rootProject.name = "FitMate"
include(":app")
include(":domain")
include(":data")
include(":presentation:designsystem")
include(":presentation:auth")
include(":presentation:home")
include(":presentation:routine")
include(":presentation:workout")
include(":presentation:record")
include(":presentation:mypage")
