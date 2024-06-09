plugins {
    packetevents.`shadow-conventions`
    packetevents.`library-conventions`
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.netty)
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
    shadow(project(":api", "shadow"))
    implementation(project(":netty-common"))
}