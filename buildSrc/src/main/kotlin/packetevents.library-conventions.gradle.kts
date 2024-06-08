import com.github.jengelman.gradle.plugins.shadow.ShadowExtension

plugins {
    `java-library`
    `maven-publish`
}

group = rootProject.group
version = rootProject.version
description = rootProject.description

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/groups/public/")
}

val isShadow = project.pluginManager.hasPlugin("com.github.johnrengelman.shadow")

dependencies {
    val annotationVersion = "23.0.0"
    add(if (isShadow) "shadow" else "compileOnly", "org.jetbrains:annotations:$annotationVersion")
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
        create<MavenPublication>("shadow") {
            groupId = "$group.${rootProject.name}"
            artifactId = project.name
            version = project.version as String

            if (isShadow) {
                project.extensions.getByName<ShadowExtension>("shadow").component(this)
                artifact(tasks["sourcesJar"])
            } else {
                from(components["java"])
            }

            pom {
                name = "${rootProject.name}-${project.name}"
                description = rootProject.description
                url = "https://github.com/retrooper/packetevents"
                licenses {
                    license {
                        name = "GPL-3.0"
                        url = "https://www.gnu.org/licenses/gpl-3.0.html"
                    }
                }
                developers {
                    developer {
                        id = "retrooper"
                        name = "Retrooper"
                        email = "retrooperdev@gmail.com"
                    }
                }
                scm {
                    connection = "scm:git:https://github.com/retrooper/packetevents.git"
                    developerConnection = "scm:git:https://github.com/retrooper/packetevents.git"
                    url = "https://github.com/retrooper/packetevents/tree/2.0"
                }
            }
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