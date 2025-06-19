import java.io.ByteArrayOutputStream

// TODO UPDATE
val fullVersion = "2.9.0"
val snapshot = true

group = "ac.grim.packetevents"
description = rootProject.name

fun getVersionMeta(includeHash: Boolean): String {
    if (!snapshot) return ""
    var commitHash = ""
    if (includeHash) {
        val stdout = ByteArrayOutputStream()
        ProcessBuilder("git", "rev-parse", "--short", "HEAD")
            .redirectErrorStream(true)
            .start()
            .apply { waitFor() }
            .inputStream.use { stdout.writeBytes(it.readAllBytes()) }
        commitHash = "+${stdout.toString().trim()}-SNAPSHOT"
    }
    return commitHash
}

version = "$fullVersion${getVersionMeta(true)}"
ext["versionNoHash"] = version

tasks {
    wrapper {
        gradleVersion = "8.13"
        distributionType = Wrapper.DistributionType.ALL
    }

    val taskSubModules: (String) -> Array<Task> = { task ->
        subprojects.filterNot { it.path == ":patch" }.map { it.tasks[task] }.toTypedArray()
    }

    register("build") {
        dependsOn(*taskSubModules("build"))
        group = "build"

        doLast {
            val buildOut = project.layout.buildDirectory.dir("libs").get().asFile
            if (!buildOut.exists())
                buildOut.mkdirs()

            for (subproject in subprojects) {
                if (subproject.path.startsWith(":patch")) continue
                val subIn = subproject.layout.buildDirectory.dir("libs").get()

                copy {
                    from(subIn)
                    into(buildOut)
                }
            }
        }
    }

    register<Delete>("clean") {
        dependsOn(*taskSubModules("clean"))
        group = "build"
        delete(rootProject.layout.buildDirectory)
    }

    defaultTasks("build")
}

allprojects {
    tasks {
        // compileJava
        withType<JavaCompile> {
            options.isFork = true
        }
        // compileTestJava
        withType<Test> {
            maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
        }
        withType<Jar> {
            archiveVersion = rootProject.ext["versionNoHash"] as String
        }
    }
}
