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
    shadow(libs.netty)
    shadow(libs.velocity)
    annotationProcessor(libs.velocity)
    api(project(":api", "shadow"))
    implementation(project(":netty-common"))
    //Velocity ships with adventure & gson
    shadow(libs.bundles.adventure)
    //Ship with legacy adventure
    implementation(libs.adventure.text.serializer.json.legacy)
}