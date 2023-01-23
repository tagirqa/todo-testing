import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    id("io.qameta.allure") version "2.11.2"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp:okhttp:2.7.5")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.1")

    testImplementation(kotlin("test"))
    testImplementation ("org.spekframework.spek2:spek-dsl-jvm:2.0.17")
    testRuntimeOnly ("org.spekframework.spek2:spek-runner-junit5:2.0.17")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}



application {
    mainClass.set("MainKt")
}