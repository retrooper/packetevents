plugins {
    packetevents.`shadow-conventions`
    packetevents.`library-conventions`
}

repositories {
    maven("https://repo.glaremasters.me/repository/public/") // brigadier 1.2.9 for BungeeCord 1.21 API
}

dependencies {
    compileOnly(libs.bungeecord)
    shadow(libs.bundles.adventure)
    shadow(project(":api", "shadow"))
    shadow(project(":netty-common"))
}
