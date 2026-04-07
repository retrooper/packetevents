import net.fabricmc.loom.task.AbstractRemapJarTask

plugins {
    packetevents.`library-conventions`
    net.fabricmc.`fabric-loom-remap`
}

repositories {
    maven("https://maven.fabricmc.net/")
    maven("https://maven.parchmentmc.org/") {
        content {
            includeGroup("org.parchmentmc.data")
        }
    }
}

dependencies {
    api(project(":fabric-common"))

    minecraft(libs.fabric.minecraft.intermediary)
    mappings(loom.layered {
        officialMojangMappings()
        parchment(libs.parchment.mappings)
    })

    modCompileOnly(libs.fabric.loader)
}

tasks {
    withType<JavaCompile> {
        options.release = 21
    }

    withType<AbstractRemapJarTask> {
        destinationDirectory = rootProject.layout.buildDirectory.dir("libs")
        archiveBaseName = "${rootProject.name}-${project.name}"
        archiveVersion = rootProject.ext["versionNoHash"] as String
    }
}

loom {
    splitEnvironmentSourceSets()
    mods {
        register("packetevents") {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets.maybeCreate("client"))
        }
    }
    accessWidenerPath = sourceSets.main.get().resources.srcDirs.single()
        .resolve("${rootProject.name}.accesswidener")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
