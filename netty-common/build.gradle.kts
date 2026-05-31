plugins {
    packetevents.`library-conventions`
}

dependencies {
    compileOnlyApi(libs.netty)
    implementation(project(":api"))
}
