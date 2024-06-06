plugins {
    java
    com.github.johnrengelman.shadow
}

tasks {
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