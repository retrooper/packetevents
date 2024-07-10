plugins {
    packetevents.`shadow-conventions`
    packetevents.`library-conventions`
    alias(libs.plugins.run.paper)
}

repositories {
    maven("https://jitpack.io")
    maven("https://repo.viaversion.com/")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.netty)
    shadow(libs.bundles.adventure)
    shadow(project(":api", "shadow"))
    shadow(project(":netty-common"))

    compileOnly(libs.paper)
    compileOnly(libs.via.version)
    compileOnly(libs.protocol.support)
}

tasks {
    shadowJar {
        // Paper doesn't need to map spigot -> mojang since we support both
        manifest {
            attributes["paperweight-mappings-namespace"] = "mojang"
        }
    }

    // 1.8.8 - 1.16.5 = Java 8
    // 1.17           = Java 16
    // 1.18 - 1.20.4  = Java 17
    // 1-20.5+        = Java 21
    val version = "1.21"
    val javaVersion = JavaLanguageVersion.of(21)

    val jvmArgsExternal = listOf(
        "-Dcom.mojang.eula.agree=true"
    )

    withType<Javadoc> {
        // javadoc generation tries to load some random nms classes and fails
        exclude("io/github/retrooper/packetevents/util/protocolsupport/**")
    }

    runServer {
        minecraftVersion(version)
        runDirectory = rootDir.resolve("run/paper/$version")

        javaLauncher = project.javaToolchains.launcherFor {
            languageVersion = javaVersion
        }

        jvmArgs = jvmArgsExternal
    }

    runPaper.folia.registerTask {
        minecraftVersion(version)
        runDirectory = rootDir.resolve("run/folia/$version")

        javaLauncher = project.javaToolchains.launcherFor {
            languageVersion = javaVersion
        }

        jvmArgs = jvmArgsExternal
    }
}

