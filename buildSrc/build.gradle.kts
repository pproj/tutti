plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.ajoberstar.grgit:grgit-core:5.0.0")
    //implementation("io.swagger.parser.v3:swagger-parser-v3:2.1.12")
    //implementation("com.squareup:kotlinpoet:1.12.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
