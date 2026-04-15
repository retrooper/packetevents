plugins {
    packetevents.`library-conventions`
}

dependencies {
    compileOnlyApi(libs.netty)
    api(project(":api"))
}
