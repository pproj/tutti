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
    // For the generated OpenAPI client
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    implementation("com.squareup.moshi:moshi-adapters:1.14.0")

    // CLI tooling
    implementation("com.github.ajalt.clikt:clikt:3.5.2")

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

    val fatJar = register<Jar>("fatJar") {
        dependsOn.addAll(listOf("compileJava", "compileKotlin", "processResources"))
        archiveClassifier.set("standalone") // Naming the jar
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest { attributes(mapOf("Main-Class" to application.mainClass)) }
        val sourcesMain = sourceSets.main.get()
        val contents =
            configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) } + sourcesMain.output
        from(contents)
    }
    jar {
        dependsOn(fatJar)
    }
}

openApiGenerate {
    generatorName.set("kotlin")
    inputSpec.set("$buildDir/tutter/apidoc/spec.yaml")
    outputDir.set("$buildDir/generated/")
    configFile.set("$rootDir/src/main/resources/tutter-api-client-config.json")
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
