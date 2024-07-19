plugins {
    packetevents.`shadow-conventions`
    packetevents.`library-conventions`
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/") // Brigadier 1.2.9
}

dependencies {
    compileOnly(libs.bungeecord)
    shadow(libs.bundles.adventure)
    shadow(project(":api", "shadow"))
    shadow(project(":netty-common"))
}
