import xyz.jpenilla.runvelocity.task.RunVelocity

plugins {
    packetevents.`shadow-conventions`
    packetevents.`library-conventions`
    packetevents.`publish-conventions`
    xyz.jpenilla.`run-velocity`
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
    shadow(project(":api", "shadow"))
    shadow(project(":netty-common"))
    compileShadowOnly(libs.bstats.velocity)
    // Velocity already bundles with adventure
}

tasks {
    named<RunVelocity>("runVelocity") {
        velocityVersion("3.3.0-SNAPSHOT")
        runDirectory = rootDir.resolve("run/velocity/")

        javaLauncher = project.javaToolchains.launcherFor {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }
}
