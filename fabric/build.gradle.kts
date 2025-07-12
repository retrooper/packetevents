import me.modmuss50.mpp.ModPublishExtension
import me.modmuss50.mpp.PublishModTask
import net.fabricmc.loom.task.RemapJarTask
import net.fabricmc.loom.task.RemapSourcesJarTask
import net.fabricmc.loom.task.prod.ServerProductionRunTask

plugins {
    packetevents.`library-conventions`
    packetevents.`publish-conventions`
    alias(libs.plugins.fabric.loom)
}

repositories {
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    maven {
        name = "ParchmentMC"
        url = uri("https://maven.parchmentmc.org")
    }
}

val minecraft_version: String by project
val parchment_minecraft_version: String by project
val parchment_mappings: String by project
val loader_version: String by project

dependencies {
    api(libs.bundles.adventure)
    api(project(":api"))
    api(project(":netty-common"))

    include(libs.bundles.adventure)
    include(project(":api"))
    include(project(":netty-common"))

    // To change the versions, see the gradle.properties file
    minecraft("com.mojang:minecraft:$minecraft_version")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-$parchment_minecraft_version:$parchment_mappings")
    })

    modImplementation("net.fabricmc:fabric-loader:$loader_version")
}

tasks {
    withType<JavaCompile> {
        val targetJavaVersion = 17
        if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
            options.release = targetJavaVersion
        }
    }

    remapJar {
        destinationDirectory = rootProject.layout.buildDirectory.dir("libs")
        archiveBaseName = "${rootProject.name}-${project.name}"
        archiveVersion = rootProject.ext["versionNoHash"] as String
    }

    remapSourcesJar {
        archiveBaseName = "${rootProject.name}-${project.name}"
        archiveVersion = rootProject.ext["versionNoHash"] as String
    }

    withType<PublishModTask> {
        dependsOn(named<RemapJarTask>("remapJar"))
        dependsOn(named<RemapSourcesJarTask>("remapSourcesJar"))
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

configure<ModPublishExtension> {
    file = tasks.named<RemapJarTask>("remapJar").flatMap { it.archiveFile }
    additionalFiles.from(tasks.named<RemapSourcesJarTask>("remapSourcesJar").flatMap { it.archiveFile })
}

tasks.register<ServerProductionRunTask>("prodServer") {
    javaLauncher = javaToolchains.launcherFor {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
