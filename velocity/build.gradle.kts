import xyz.jpenilla.runvelocity.task.RunVelocity

plugins {
    packetevents.`shadow-conventions`
    packetevents.`library-conventions`
    packetevents.`publish-conventions`
    xyz.jpenilla.`run-velocity`
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    api(project(":api"))
    api(project(":netty-common"))

    compileOnly(libs.netty)
    implementation(libs.bstats.velocity)

    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
}

tasks {
    named<RunVelocity>("runVelocity") {
        velocityVersion("3.4.0-SNAPSHOT")
        runDirectory = rootDir.resolve("run/velocity/")

        javaLauncher = project.javaToolchains.launcherFor {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }
}
