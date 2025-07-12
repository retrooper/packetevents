plugins {
    packetevents.`shadow-conventions`
    packetevents.`library-conventions`
}

dependencies {
    compileOnly(libs.netty)
    apiAndPublish(project(":api"))
}
