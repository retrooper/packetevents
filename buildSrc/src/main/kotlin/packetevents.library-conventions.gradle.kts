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
    disableAutoTargetJvm()
}

tasks {
    withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
        // See https://openjdk.java.net/jeps/247 for more information.
        options.release = 8
    }

    jar {
        archiveClassifier = "default"
    }

    defaultTasks("build")
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

            val mavenUsername = System.getenv("retrooper_username") ?: return@maven
            val mavenPassword = System.getenv("retrooper_password") ?: return@maven

            credentials {
                username = mavenUsername
                password = mavenPassword
            }
        }
    }
}

// So that SNAPSHOT is always the latest SNAPSHOT
configurations.all {
    resolutionStrategy.cacheDynamicVersionsFor(0, "seconds")
}