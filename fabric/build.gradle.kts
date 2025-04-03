plugins {
    packetevents.`library-conventions`
    alias(libs.plugins.fabric.loom)
}

repositories {
    mavenCentral()
    maven("https://repo.viaversion.com/")
    maven("https://jitpack.io") // Conditional Mixin
}

val minecraft_version: String by project
val yarn_mappings: String by project
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
    mappings("net.fabricmc:yarn:$yarn_mappings")

    compileOnly(libs.via.version)
}

loom {
    mods {
        register("packetevents-${project.name}") {
            sourceSet(sourceSets.main.get())
        }
    }
}

allprojects {
    apply(plugin = "fabric-loom")

    repositories {
        maven("https://repo.codemc.io/repository/maven-snapshots/")
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

        remapJar {
            archiveBaseName = "${rootProject.name}-fabric${if (project.name != "fabric") "-${project.name}" else ""}"
            archiveVersion = rootProject.ext["versionNoHash"] as String
        }

        remapSourcesJar {
            archiveVersion = rootProject.ext["versionNoHash"] as String
        }
    }

    loom {
        mixin {
        // Replaces strings in annotations instead of using refmap
        // This allows us to write mixins that target methodName* and have them work across versions
        // Even as the signature changes without having to use @Dynamic and intermeediary names
        // This preserves some compile-time safety, reduces jar size but be careful to not inject into wrong methods
            useLegacyMixinAp = false
        }

        accessWidenerPath = sourceSets.main.get().resources.srcDirs.single()
            .resolve("${rootProject.name}.accesswidener")
    }
}

subprojects {
    version = rootProject.version

    repositories {
        maven {
            name = "ParchmentMC"
            url = uri("https://maven.parchmentmc.org")
        }
    }

    dependencies {
        compileOnly(project(":api", "shadow"))
        compileOnly(project(":netty-common"))
        compileOnly(project(":fabric", configuration = "namedElements"))
    }

    loom {
        splitEnvironmentSourceSets()
        mods {
            register("packetevents-${project.name}") {
                sourceSet(sourceSets.main.get())
                sourceSet(sourceSets.maybeCreate("client"))
            }
        }
    }

    // version replacement already processed for :fabric in packetevents.`library-conventions`
    tasks {
        processResources {
            inputs.property("version", project.version)
            filesMatching("fabric.mod.json") {
                expand("version" to project.version)
            }
        }
    }
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
