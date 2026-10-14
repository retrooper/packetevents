import xyz.jpenilla.runwaterfall.task.RunWaterfall

plugins {
    packetevents.`shadow-conventions`
    packetevents.`library-conventions`
    packetevents.`publish-conventions`
    xyz.jpenilla.`run-waterfall`
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.bungeecord)
    shadow(libs.bundles.adventure)
    compileShadowOnly(libs.bstats.bungeecord)
    shadow(project(":api", "shadow"))
    shadow(project(":netty-common"))
}

tasks {
    named<RunWaterfall>("runWaterfall") {
        val mcVersion = "1.21"
        waterfallVersion(mcVersion)
        runDirectory = rootDir.resolve("run/waterfall/$mcVersion")
    }
}
