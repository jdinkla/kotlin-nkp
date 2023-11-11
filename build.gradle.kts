val kotestVersion = "5.8.0"

plugins {
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.serialization") version "1.9.20"
    id("io.gitlab.arturbosch.detekt") version "1.23.3"
    application
}

group = "net.dinkla"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("org.jetbrains.kotlin.spec.grammar.tools:kotlin-grammar-tools:0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("net.dinkla.kpnk.MainKt")
}

tasks.named<JavaExec>("run") {
    doFirst {
        args = if (!project.hasProperty("args")) {
            listOf("src/test/resources/example")
        } else {
            (project.property("args") as String).split(",")
        }
    }
}
