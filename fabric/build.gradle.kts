plugins {
    packetevents.`library-conventions`
    alias(libs.plugins.fabric)
}

repositories {
    mavenCentral()
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    maven {
        name = "ParchmentMC"
        url = uri("https://maven.parchmentmc.org")
    }
}

val includeAll: Configuration by configurations.creating

val minecraft_version: String by project
val parchment_minecraft_version: String by project
val parchment_mappings: String by project
val fabric_version: String by project
val loader_version: String by project

dependencies {
    api(libs.bundles.adventure)
    api(project(":api", "shadow"))
    api(project(":netty-common"))

    include(project(":api", "shadow"))
    include(project(":netty-common"))

    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:$minecraft_version")

    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-$parchment_minecraft_version:$parchment_mappings")
    })

    modImplementation("net.fabricmc:fabric-loader:$loader_version")

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabric_version")
}

val targetJavaVersion = 17
tasks {
    processResources {
        inputs.property("version", project.version)
        filteringCharset = Charsets.UTF_8.name()

        filesMatching("fabric.mod.json") {
            expand(mapOf("version" to project.version))
        }
    }

    withType<JavaCompile> {
        // ensure that the encoding is set to UTF-8, no matter what the system default is
        // this fixes some edge cases with special characters not displaying correctly
        // see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
        // If Javadoc is generated, this must be specified in that task too.
        if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
            options.release = targetJavaVersion
        }
    }

    remapJar {
        archiveBaseName.set("${rootProject.name}-fabric")
    }
}