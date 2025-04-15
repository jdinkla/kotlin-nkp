import de.fayard.refreshVersions.core.StabilityLevel

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
    id("de.fayard.refreshVersions") version "0.60.5"
}

rootProject.name = "kotlin-nkp"

refreshVersions {
    rejectVersionIf {
        candidate.stabilityLevel != StabilityLevel.Stable
    }
}