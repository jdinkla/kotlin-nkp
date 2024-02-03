pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
    id("de.fayard.refreshVersions") version "0.60.4"
}

rootProject.name = "kotlin-nkp"
