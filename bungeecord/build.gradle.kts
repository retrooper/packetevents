plugins {
    packetevents.`library-conventions`
    packetevents.`shadow-conventions`
}

dependencies {
    compileOnly(libs.bungeecord)
    api(project(":api"))
    api(project(":netty-common"))
    api(libs.bundles.adventure)
}

tasks {
    shadowJar {
        relocate("net.kyori.adventure.text.serializer.gson", "io.github.retrooper.packetevents.adventure.serializer.gson")
        relocate("net.kyori.adventure.text.serializer.legacy", "io.github.retrooper.packetevents.adventure.serializer.legacy")
        dependencies {
            exclude(dependency("com.google.code.gson:gson:2.8.0"))
        }
    }
}
