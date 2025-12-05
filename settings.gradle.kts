pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("com.android.application") version "8.7.0" // или 8.5.2 — главное новее 8.5
        id("org.jetbrains.kotlin.android") version "1.9.24"
        id("com.google.dagger.hilt.android") version "2.51"
        id("org.jetbrains.kotlin.kapt") version "1.9.24"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "PayAble"
include(":app")