plugins {
    kotlin("jvm") version "1.8.20"
    id("org.openapi.generator") version "6.5.0"
    application
}

group = "net.pproj"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    implementation("com.squareup.moshi:moshi-adapters:1.14.0")
    testImplementation(kotlin("test"))
}

// -----------
// Build magic
// -----------
apply<TuttiBuildPlugin>()
val tutterRepo: String by project

tasks {
    test {
        useJUnitPlatform()
    }

    compileKotlin {
        dependsOn("openApiGenerate")
    }

    findByName("downloadTutterRepo")?.setProperty("repoUrl", tutterRepo)

    named("openApiGenerate") {
        dependsOn("downloadTutterRepo")
    }
}

openApiGenerate {
    generatorName.set("kotlin")
    inputSpec.set("$buildDir/tutter/apidoc/spec.yaml")
    outputDir.set("$buildDir/generated/")
    configFile.set("$rootDir/src/main/resources/api-config.json")
    packageName.set("net.pproj.tutter.client")
}

sourceSets {
    main {
        kotlin {
            srcDir("$buildDir/generated/src/main/kotlin")
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}
