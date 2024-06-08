plugins {
    packetevents.`shadow-conventions`
    packetevents.`library-conventions`
}

dependencies {
    shadow(libs.netty)
    api(project(":api", "shadow"))
}