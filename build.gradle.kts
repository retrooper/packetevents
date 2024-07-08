group = "com.github.retrooper"
description = rootProject.name
version = "2.4.1-SNAPSHOT" //TODO UPDATE - ADD "-SNAPSHOT" if we are dealing with snapshot versions

tasks {
    wrapper {
        gradleVersion = "8.5"
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
