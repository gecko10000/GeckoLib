import org.gradle.launcher.daemon.protocol.Build

plugins {
    kotlin("jvm") version "1.9.22"
    id("java-library")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "gecko10000.geckolib"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(kotlin("stdlib", version = "1.9.22"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")

    //implementation("io.insert-koin:koin-core:3.5.3")

    compileOnly("com.charleskorn.kaml:kaml:0.57.0")
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks {
    build {
        dependsOn(publishToMavenLocal, shadowJar)
    }
}

sourceSets {
    main {
        java {
            srcDir("src")
        }
        resources {
            srcDir("res")
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("local") {
            from(components["java"])
            artifactId = "GeckoLib"
        }
    }
}

kotlin {
    jvmToolchain(17)
}

tasks.register("update") {
    dependsOn(tasks.build)
    doLast {
        exec {
            workingDir(".")
            commandLine("./update.sh")
        }
    }
}