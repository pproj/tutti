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

// -----------
// Build magic
// -----------
apply<TuttiBuildPlugin>()

val tutterRepo: String by project
tasks.findByName("downloadTutterRepo")?.setProperty("repoUrl", tutterRepo)

val tutterApiClient = "tutter-client"
val generatedSrc = "src/generated/kotlin"

dependencies {
    testImplementation(kotlin("test"))
    //implementation(project(":$tutterApiClient"))
//project(":tutter-api")
}

tasks {
    val clean by getting {
        doFirst {
            delete("$projectDir/$generatedSrc")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.named("compileKotlin") {
    dependsOn("openApiGenerate")
}

tasks.named("openApiGenerate") {
    dependsOn("downloadTutterRepo")
}

val tutterClientSourcePath = "$projectDir/$tutterApiClient/main"

openApiGenerate {
    generatorName.set("kotlin")
    id.set(tutterApiClient)
    inputSpec.set("$buildDir/tutter/apidoc/spec.yaml")
    //remoteInputSpec.set("https://playground.ashdavies.dev/openapi/documentation.yml")
    //outputDir.set(tutterClientSourcePath)
    outputDir.set(projectDir.toString())
    additionalProperties.put("sourceFolder", generatedSrc)
    packageName.set("net.pproj.tutter.client")
    configOptions.set(
        mapOf(
            "library" to "jvm-ktor",
            "dateLibrary" to "java8",
            "hideGenerationTimestamp" to "true",
            //"openApiNullable" to "false",
            "useBeanValidation" to "false",
            "sourceFolder" to "",  // makes IDEs like IntelliJ more reliably interpret the class packages.
            "containerDefaultToNull" to "true"
        )
    )
}

//sourceSets {
//    main {
//        kotlin {
//            srcDir(tutterClientSourcePath)
//        }
//    }
//}

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
