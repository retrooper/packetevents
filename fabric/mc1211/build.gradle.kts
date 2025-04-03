val minecraft_version: String by project
val yarn_mappings: String by project

dependencies {
    compileOnly(project(":fabric:mc1202", configuration = "namedElements"))
    compileOnly(project(":fabric:mc1201", configuration = "namedElements"))

    // To change the versions, see the gradle.properties file
    minecraft("com.mojang:minecraft:$minecraft_version")
    mappings("net.fabricmc:yarn:$yarn_mappings")
}