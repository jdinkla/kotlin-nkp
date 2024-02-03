val kotestVersion = "5.8.0"

plugins {
    kotlin("jvm") version "2.0.0-Beta3"
    kotlin("plugin.serialization") version "2.0.0-Beta3"
    id("io.gitlab.arturbosch.detekt")
    application
}

group = "net.dinkla"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(KotlinX.coroutines.core)
    implementation(KotlinX.serialization.json)
    implementation("org.jetbrains.kotlin.spec.grammar.tools:kotlin-grammar-tools:_")
    implementation("org.slf4j:slf4j-api:_")
    implementation("ch.qos.logback:logback-classic:_")

    testImplementation(Testing.kotest.runner.junit5)
    testImplementation(Testing.kotest.assertions.core)
    testImplementation(Testing.kotest.framework.datatest)
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
        args =
            if (!project.hasProperty("args")) {
                listOf("src/test/resources/example")
            } else {
                (project.property("args") as String).split(",")
            }
    }
}
