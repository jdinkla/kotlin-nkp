plugins {
    val kotlinVersion = libs.versions.kotlinVersion.get()
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    alias(libs.plugins.detekt)
    jacoco
    application
}

group = "net.dinkla"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.serialization)
    implementation(libs.clikt)
    implementation(libs.kotlin.grammar.tools)
    implementation(libs.kotlin.logging.jvm)

    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.framework.datatest)
    testImplementation(libs.archunit)
    testImplementation(libs.archunit.junit5)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}
tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

application {
    mainClass.set("net.dinkla.nkp.MainKt")
}
