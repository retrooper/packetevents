plugins {
    packetevents.`shadow-conventions`
    packetevents.`library-conventions`
}

dependencies {
    compileOnly(libs.netty)
    api(project(":api", "shadow"))
}