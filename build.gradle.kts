group = "com.github.retrooper"
description = rootProject.name
version = "2.3.1-SNAPSHOT" //TODO UPDATE - ADD "-SNAPSHOT" if we are dealing with snapshot versions

tasks {
    wrapper {
        gradleVersion = "8.5"
        distributionType = Wrapper.DistributionType.ALL
    }

    register("build") {
        dependsOn(*subprojects.map { it.tasks["build"] }.toTypedArray())
        group = "build"

        doLast {
            val buildOut = project.layout.buildDirectory.dir("libs").get()
            for (subproject in subprojects) {
                val subIn = subproject.layout.buildDirectory.dir("libs").get()
                val subOut = buildOut.dir(subproject.name).asFile
                if (!subOut.exists())
                    subOut.mkdirs()

                copy {
                    from(subIn)
                    into(subOut)
                }
            }
        }
    }

    defaultTasks("build")
}
