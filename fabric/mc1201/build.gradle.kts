val minecraft_version: String by project
val parchment_minecraft_version: String by project
val parchment_mappings: String by project

dependencies {
    implementation(libs.bundles.adventure)

    // To change the versions, see the gradle.properties file
    minecraft("com.mojang:minecraft:$minecraft_version")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-$parchment_minecraft_version:$parchment_mappings")
    })
}

loom {
    splitEnvironmentSourceSets()
    mods {
        register("packetevents-mc1201") {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets.maybeCreate("client"))
        }
    }
    accessWidenerPath = sourceSets.main.get().resources.srcDirs.single()
        .resolve("${rootProject.name}.accesswidener")
}