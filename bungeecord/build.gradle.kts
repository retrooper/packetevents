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
    api(project(":api"))
    api(project(":netty-common"))

    compileOnly(libs.bungeecord)
    implementation(libs.bstats.bungeecord)
}

tasks {
    named<RunWaterfall>("runWaterfall") {
        val mcVersion = "1.21"
        waterfallVersion(mcVersion)
        runDirectory = rootDir.resolve("run/waterfall/$mcVersion")
    }
}
