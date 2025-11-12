plugins {
    packetevents.`publish-conventions`
}

// properties are all set as string, convert to boolean
ext["snapshot"] = ext["snapshot"].toString().toBooleanStrict()

ext["commitHash"] = providers.exec {
    commandLine("git", "rev-parse", "--short", "HEAD")
}.standardOutput.asText.map { it.trim() }.getOrElse("unknown")
ext["versionMeta"] = if (ext["snapshot"] == true) "-SNAPSHOT" else ""
ext["versionMetaWithHash"] = "+${ext["commitHash"]}${ext["versionMeta"]}"
ext["versionNoHash"] = "${ext["fullVersion"]}${ext["versionMeta"]}"

group = "com.github.retrooper"
description = rootProject.name
version = "${ext["fullVersion"]}${ext[if (ext["snapshot"] == true) "versionMetaWithHash" else "versionMeta"]}"

tasks {
    val taskSubModules: (String) -> Array<Task> = { task ->
        subprojects.filterNot { it.path == ":patch" }.map { it.tasks[task] }.toTypedArray()
    }

    register<Delete>("clean") {
        dependsOn(*taskSubModules("clean"))
        delete(rootProject.layout.buildDirectory)
    }

    register("printVersion") {
        println(project.version)
    }

    defaultTasks("build")
}

allprojects {
    tasks {
        withType<Jar> {
            archiveBaseName = "${rootProject.name}-${project.name}"
            archiveVersion = rootProject.ext["versionNoHash"] as String
        }
    }
}
