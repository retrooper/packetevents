plugins {
    packetevents.`shadow-conventions`
    packetevents.`library-conventions`
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.bungeecord)
    shadow(libs.bundles.adventure)
    shadow(project(":api", "shadow"))
    shadow(project(":netty-common"))
}
