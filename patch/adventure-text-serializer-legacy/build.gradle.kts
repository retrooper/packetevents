plugins {
    packetevents.`patching-conventions`
}

dependencies {
    api(libs.adventure.text.serializer.legacy)
    api(libs.adventure.text.serializer.commons)
}

tasks {
    shadowJar {
        dependencies {
            exclude(dependency("net.kyori:adventure-api:.*"))
            exclude(dependency("net.kyori:adventure-key:.*"))
            exclude(dependency("net.kyori:adventure-nbt:.*"))
            exclude(dependency("net.kyori:examination-api:.*"))
            exclude(dependency("net.kyori:examination-string:.*"))
            exclude("META-INF/services/**")
        }
    }
}
