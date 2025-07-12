plugins {
    packetevents.`shadow-conventions`
    packetevents.`library-conventions`
    packetevents.`publish-conventions`
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.bungeecord)
    apiAndPublish(libs.bundles.adventure)

    apiAndPublish(project(":api"))
    apiAndPublish(project(":netty-common"))
    shadowAndPublish(libs.bstats.bungeecord)
}
