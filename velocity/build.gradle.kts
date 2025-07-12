plugins {
    packetevents.`shadow-conventions`
    packetevents.`library-conventions`
    packetevents.`publish-conventions`
    alias(libs.plugins.run.velocity)
}

repositories {
    mavenCentral()
   // maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.netty)
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)

    apiAndPublish(project(":api"))
    apiAndPublish(project(":netty-common"))
    shadowAndPublish(libs.bstats.velocity)
}

tasks {
    runVelocity {
        velocityVersion("3.3.0-SNAPSHOT")
        runDirectory = rootDir.resolve("run/velocity/")

        javaLauncher = project.javaToolchains.launcherFor {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }
}
