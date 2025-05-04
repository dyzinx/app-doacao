pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        // Remova o jcenter() se possível (está obsoleto)
        maven("https://jitpack.io")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

rootProject.name = "Tela de Login"
include(":app")