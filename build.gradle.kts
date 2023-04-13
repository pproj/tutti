plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "net.pproj"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

apply<TuttiBuildPlugin>()

val tutterRepo: String by project
tasks.findByName("downloadTutterRepo")?.setProperty("repoUrl", tutterRepo)

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}
