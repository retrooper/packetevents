plugins {
    packetevents.`library-conventions`
    packetevents.`shadow-conventions`
}

dependencies {
    compileOnly(libs.netty)
    api(project(":api"))
}