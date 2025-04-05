val minecraft_version: String by project
val yarn_mappings: String by project

repositories {
    mavenCentral()
}

dependencies {
    // To change the versions, see the gradle.properties file
    minecraft("com.mojang:minecraft:$minecraft_version")
    mappings("net.fabricmc:yarn:$yarn_mappings")
}

loom {
    splitEnvironmentSourceSets()
    mods {
        register("packetevents-${project.name}") {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets.maybeCreate("client"))
        }
    }
}