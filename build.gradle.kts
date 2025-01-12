plugins {
    kotlin("jvm") version "2.0.21"
    id("java-library")
    id("maven-publish")
    kotlin("plugin.serialization") version "1.4.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "gecko10000.geckolib"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://redempt.dev/")
}

dependencies {
    api(kotlin("stdlib", version = "2.0.21"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("net.kyori:adventure-platform-bukkit:4.3.2")
    compileOnly("com.github.Redempt:RedLib:6.6.1")
    api("io.insert-koin:koin-core:3.5.3") {
        exclude("org.jetbrains.kotlin")
    }
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    api("com.charleskorn.kaml:kaml:0.67.0")
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
    jvmToolchain(21)
}

tasks.register("update") {
    dependsOn(tasks.build)
    doLast {
        exec {
            workingDir(".")
            commandLine("../../dot/local/bin/update.sh")
        }
    }
}
