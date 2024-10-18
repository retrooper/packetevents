import java.io.ByteArrayOutputStream

// TODO UPDATE
val fullVersion = "2.5.1"
val snapshot = true

group = "com.github.retrooper"
description = rootProject.name

fun getVersionMeta(includeHash: Boolean): String {
    if (!snapshot) {
        return ""
    }
    var commitHash = ""
    if (includeHash && file(".git").isDirectory) {
        val stdout = ByteArrayOutputStream()
        exec {
            commandLine("git", "rev-parse", "--short", "HEAD")
            standardOutput = stdout
        }
        commitHash = "+${stdout.toString().trim()}"
    }
    return "$commitHash-SNAPSHOT"
}
version = "$fullVersion${getVersionMeta(true)}"
ext["versionNoHash"] = "$fullVersion${getVersionMeta(false)}"

tasks {
    wrapper {
        gradleVersion = "8.10"
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
        withType<Jar> {
            archiveVersion = rootProject.ext["versionNoHash"] as String
        }

        withType<Javadoc> {
            title = "packetevents-${project.name} v${rootProject.version}"
            options.encoding = Charsets.UTF_8.name()
            options.overview = rootProject.file("buildSrc/src/main/resources/javadoc-overview.html").toString()
        }
    }
}
