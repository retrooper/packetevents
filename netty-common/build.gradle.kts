plugins {
    packetevents.`library-conventions`
}

dependencies {
    compileOnly(libs.netty)
    implementation(project(":api", "shadow"))
}