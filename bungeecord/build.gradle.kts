plugins {
    packetevents.`shadow-conventions`
    packetevents.`library-conventions`
}

dependencies {
    compileOnly(libs.bungeecord)
    shadow(project(":api", "shadow"))
    shadow(project(":netty-common"))
}
