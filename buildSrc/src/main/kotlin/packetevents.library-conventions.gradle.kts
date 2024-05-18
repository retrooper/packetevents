plugins {
    `java-library`
    `maven-publish`
}

group = rootProject.group
version = rootProject.version
description = rootProject.description

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    compileOnly("org.jetbrains:annotations:23.0.0")
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
        // See https://openjdk.java.net/jeps/247 for more information.
        options.release.set(8)
    }

    jar {
        archiveClassifier = "default"
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "$group.$name"
            artifactId = project.name
            version = project.version as String

            from(components["java"])
        }
    }

    repositories {
        maven {
            val snapshotUrl = "https://repo.codemc.io/repository/maven-snapshots/"
            val releaseUrl = "https://repo.codemc.io/repository/maven-releases/"

            // Check which URL should be used
            url = uri(if ((version as String).endsWith("SNAPSHOT")) snapshotUrl else releaseUrl)

            val mavenUsername = System.getenv("retrooper_username")
            val mavenPassword = System.getenv("retrooper_password")

            if (mavenUsername != null && mavenPassword != null) {
                credentials {
                    username = mavenUsername
                    password = mavenPassword
                }
            }
        }
    }
}

// So that SNAPSHOT is always the latest SNAPSHOT
configurations.all {
    resolutionStrategy.cacheDynamicVersionsFor(0, "seconds")
}