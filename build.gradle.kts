plugins {
    kotlin("jvm") version "_"
    kotlin("plugin.serialization") version "_"
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
    implementation("com.github.ajalt.clikt:clikt:_")
    implementation("io.github.oshai:kotlin-logging-jvm:_")

    testImplementation(Testing.kotest.runner.junit5)
    testImplementation(Testing.kotest.assertions.core)
    testImplementation(Testing.kotest.framework.datatest)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("net.dinkla.nkp.MainKt")
}
