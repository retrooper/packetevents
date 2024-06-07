plugins {
    packetevents.`library-conventions`
    packetevents.`shadow-conventions`
}

dependencies {
    compileOnly(libs.bungeecord)
    api(project(":api", "shadow"))
    api(project(":netty-common"))
    api(libs.bundles.adventure)
}
