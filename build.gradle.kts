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
            val buildOut = project.layout.buildDirectory.dir("libs").get().asFile
            if (!buildOut.exists())
                buildOut.mkdirs()

            for (subproject in subprojects) {
                val subIn = subproject.layout.buildDirectory.dir("libs").get()

                copy {
                    from(subIn)
                    into(buildOut)
                }
            }
        }
    }

    register<Delete>("clean") {
        dependsOn(*subprojects.map { it.tasks["clean"] }.toTypedArray())
        group = "build"
        delete(rootProject.layout.buildDirectory)
    }

    defaultTasks("build")
}
