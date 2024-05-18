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
    }
}
