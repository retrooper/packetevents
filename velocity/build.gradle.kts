plugins {
    packetevents.`library-conventions`
    packetevents.`shadow-conventions`
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.netty)
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
    api(project(":api"))
    implementation(project(":netty-common"))
    //Velocity ships with adventure & gson
    compileOnly(libs.bundles.adventure)
    //Ship with legacy adventure
    implementation(libs.adventure.text.serializer.gson.legacy)
}

tasks {
    shadowJar {
        relocate("net.kyori.adventure.text.serializer.gson", "io.github.retrooper.packetevents.adventure.serializer.gson")
        relocate("net.kyori.adventure.text.serializer.legacy", "io.github.retrooper.packetevents.adventure.serializer.legacy")
        relocate("net.kyori.adventure.text.serializer.gson.legacyimpl", "io.github.retrooper.packetevents.adventure.serializer.gson.legacyimpl")
    }
}