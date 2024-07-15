plugins {
    packetevents.`shadow-conventions`
    packetevents.`library-conventions`
}

repositories {
    maven("https://repo.glaremasters.me/repository/public/") // brigadier
}

dependencies {
    compileOnly(libs.bungeecord)
    shadow(libs.bundles.adventure)
    shadow(project(":api", "shadow"))
    shadow(project(":netty-common"))
}
