import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import groovy.util.Node

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

val isShadow = project.pluginManager.hasPlugin("com.gradleup.shadow")

dependencies {
    compileOnly("org.jetbrains:annotations:23.0.0")
}

java {
    withSourcesJar()
    withJavadocJar()
    disableAutoTargetJvm()
}

tasks {
    withType<JavaCompile> {
        options.compilerArgs.add("-parameters")
        options.compilerArgs.add("-g")

        sequenceOf("unchecked", "deprecation", "removal")
            .forEach { options.compilerArgs.add("-Xlint:$it") }

        options.encoding = Charsets.UTF_8.name()
        // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
        // See https://openjdk.java.net/jeps/247 for more information.
        options.release = 8
    }

    javadoc {
        title = "packetevents-${project.name} v${rootProject.version}"
        options.encoding = Charsets.UTF_8.name()
        options.overview = rootProject.file("buildSrc/src/main/resources/javadoc-overview.html").toString()
        setDestinationDir(file("${project.layout.buildDirectory.asFile.get()}/docs/javadoc"))
        options {
            (this as CoreJavadocOptions).addBooleanOption("Xdoclint:none", true)
        }
    }

    processResources {
        inputs.property("version", project.version)
        filesMatching(listOf("plugin.yml", "bungee.yml", "velocity-plugin.json", "fabric.mod.json")) {
            expand("version" to project.version)
        }
    }

    jar {
        if (isShadow) {
            archiveClassifier = "default"
        } else {
            destinationDirectory = rootProject.layout.buildDirectory.dir("libs")
        }
    }

    sequenceOf("sourcesJar", "javadocJar").forEach {
        named<Jar>(it) {
            destinationDirectory = rootProject.layout.buildDirectory.dir("libs")
        }
    }

    val writeVersionFile by tasks.registering {
        val outFile = layout.buildDirectory.file("generated/${rootProject.name}_${project.name}_version.txt")
        outputs.file(outFile)

        val projectVersion = project.version.toString()

        doLast {
            outFile.map { it.asFile }.get().apply {
                parentFile.mkdirs()
                writeText(projectVersion)
            }
        }
    }

    // write version file to each jar; this solves our issue of modrinth not accepting
    // uploads of the same file twice, caused by the sources jar of some modules not changing for some versions
    withType<Jar> {
        dependsOn(writeVersionFile)
        metaInf {
            from(writeVersionFile)
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group as String
            artifactId = "${rootProject.name}-${project.name}"
            version = rootProject.ext["versionNoHash"] as String

            from(components["java"])

            pom {
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
                    developer {
                        id = "booky10"
                        name = "booky"
                        email = "booky@booky.dev"
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
    resolutionStrategy.cacheDynamicVersionsFor(0, TimeUnit.SECONDS)
}
