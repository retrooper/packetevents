plugins {
    packetevents.`patching-conventions`
}

dependencies {
    api(libs.adventure.text.serializer.gson)
    api(libs.adventure.text.serializer.json.legacy)
}

tasks {
    shadowJar {
        dependencies {
            exclude(dependency("net.kyori:adventure-api:.*"))
            exclude(dependency("net.kyori:adventure-key:.*"))
            exclude(dependency("net.kyori:adventure-nbt:.*"))
            exclude(dependency("net.kyori:examination-api:.*"))
            exclude(dependency("net.kyori:examination-string:.*"))
            exclude(dependency("com.google.code.gson:gson:.*"))
            exclude("META-INF/services/**")
        }
    }
}