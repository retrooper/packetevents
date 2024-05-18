plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.shadow)
}

allprojects {
    group = "com.github.retrooper"
    description = rootProject.name
    version = "2.3.1-SNAPSHOT" //TODO UPDATE - ADD "-SNAPSHOT" if we are dealing with snapshot versions
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    if (project.name != "fabric")
        apply(plugin = rootProject.libs.plugins.shadow.get().pluginId)

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://oss.sonatype.org/content/groups/public/")
    }

    dependencies {
        compileOnly(rootProject.libs.jetbrains.annotations)
    }

    java {
        withSourcesJar()
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks {
        if (project.name != "fabric") {
            shadowJar {
                archiveFileName.set("packetevents-${project.name}-${project.version}.jar")
                archiveClassifier = null
            }

            build {
                dependsOn(shadowJar)
            }

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
}

tasks {
    wrapper {
        gradleVersion = "8.5"
        distributionType = Wrapper.DistributionType.ALL
    }

    shadowJar {
        enabled = false
    }

    build {
        dependsOn(*subprojects.map { it.tasks.build }.toTypedArray())
    }
}
