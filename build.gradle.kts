val kotestVersion = "5.8.0"

plugins {
    kotlin("jvm") version "1.9.20"
    application
}

group = "net.dinkla"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
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

tasks.named<org.gradle.api.tasks.JavaExec>("run") {
    args = listOf("src/main/kotlin")
}
