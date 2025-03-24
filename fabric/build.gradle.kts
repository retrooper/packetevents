plugins {
    packetevents.`library-conventions`
    alias(libs.plugins.fabric.loom)
    id("idea")
}

repositories {
    maven("https://repo.viaversion.com/")
    maven("https://jitpack.io") // Conditional Mixin
}

val minecraft_version: String by project
val parchment_minecraft_version: String by project
val parchment_mappings: String by project
val loader_version: String by project

dependencies {
    api(libs.bundles.adventure)
    api(project(":api", "shadow"))
    api(project(":netty-common"))
    modApi("com.github.Fallen-Breath.conditional-mixin:conditional-mixin-fabric:0.6.4")

    include(libs.bundles.adventure)
    include(project(":api", "shadow"))
    include(project(":netty-common"))
    include("com.github.Fallen-Breath.conditional-mixin:conditional-mixin-fabric:0.6.4")

    // To change the versions, see the gradle.properties file
    minecraft("com.mojang:minecraft:$minecraft_version")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-$parchment_minecraft_version:$parchment_mappings")
    })

    compileOnly(libs.via.version)
}

allprojects {
    apply(plugin = "fabric-loom")

    repositories {
        maven("https://repo.codemc.io/repository/maven-snapshots/")
        maven {
            name = "ParchmentMC"
            url = uri("https://maven.parchmentmc.org")
        }
    }

    dependencies {
        modImplementation("net.fabricmc:fabric-loader:$loader_version")
    }

    tasks {
        withType<JavaCompile> {
            val targetJavaVersion = 17
            if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
                options.release = targetJavaVersion
            }
        }

        remapSourcesJar {
            archiveVersion = rootProject.ext["versionNoHash"] as String
        }
    }

    apply(plugin = "idea")
    idea {
        module {
            isDownloadJavadoc = true
            isDownloadSources = true
        }
    }
}

subprojects {
    version = rootProject.version

    dependencies {
        compileOnly(project(":api", "shadow"))
        compileOnly(project(":netty-common"))
        compileOnly(project(":fabric", configuration = "namedElements"))
    }

    tasks {
        remapJar {
            archiveBaseName = "${rootProject.name}-fabric-${project.name}"
            archiveVersion = rootProject.ext["versionNoHash"] as String
        }

        processResources {
            inputs.property("version", project.version)
            filesMatching("fabric.mod.json") {
                expand("version" to project.version)
            }
        }
    }
}

tasks {
    remapJar {
        archiveBaseName = "${rootProject.name}-fabric"
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

subprojects.forEach {
    tasks.named("remapJar").configure {
        dependsOn("${it.path}:remapJar")
    }
}

tasks.remapJar.configure {
    subprojects.forEach { subproject ->
        subproject.tasks.matching { it.name == "remapJar" }.configureEach {
            nestedJars.from(this)
        }
    }
}
