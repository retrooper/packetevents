plugins {
    packetevents.`shadow-conventions`
    packetevents.`library-conventions`
}

dependencies {
    compileOnly(libs.netty)
    shadow(project(":api", "shadow"))
}