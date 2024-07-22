plugins {
    packetevents.`library-conventions`
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
val fabric_version: String by project
val loader_version: String by project

dependencies {
    api(libs.bundles.adventure)
    api(project(":api", "shadow"))
    api(project(":netty-common"))

    include(libs.bundles.adventure)
    include(project(":api", "shadow"))
    include(project(":netty-common"))

    // To change the versions, see the gradle.properties file
    minecraft("com.mojang:minecraft:$minecraft_version")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-$parchment_minecraft_version:$parchment_mappings")
    })

    modImplementation("net.fabricmc:fabric-loader:$loader_version")

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabric_version")
}

tasks {
    withType<JavaCompile> {
        val targetJavaVersion = 17
        if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
            options.release = targetJavaVersion
        }
    }

    sequenceOf(remapJar, remapSourcesJar).forEach {
        it {
            archiveBaseName = "${rootProject.name}-fabric"
            archiveVersion = rootProject.ext["versionNoHash"] as String
        }
    }
}
