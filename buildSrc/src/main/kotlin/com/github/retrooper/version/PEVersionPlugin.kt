package com.github.retrooper.version

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.register

class PEVersionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val task = target.tasks.register<PEVersionTask>(PEVersionTask.TASK_NAME) {
            group = target.rootProject.name.toString()

            version = target.version.toString()
            outputDir = target.layout.buildDirectory.dir("generated/sources/src/java/main")
        }

        target.afterEvaluate {
            val sourceSets = target.extensions.getByName<SourceSetContainer>("sourceSets")

            sequenceOf(SourceSet.MAIN_SOURCE_SET_NAME, SourceSet.TEST_SOURCE_SET_NAME).forEach {
                sourceSets.getByName(it).java.srcDir(task.flatMap { it.outputDir })
            }

            task.get().generate()
        }
    }

}