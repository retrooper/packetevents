plugins {
    `java-library`
    com.gradleup.shadow
}

group = rootProject.group
version = rootProject.version
description = rootProject.description

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/groups/public/")
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
        enabled = false
    }

    shadowJar {
        isEnableRelocation = false
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveClassifier = null
    }

    assemble {
        dependsOn(shadowJar)
    }

    defaultTasks("build")
}