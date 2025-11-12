import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import me.modmuss50.mpp.ModPublishExtension
import me.modmuss50.mpp.PublishModTask

plugins {
    me.modmuss50.`mod-publish-plugin`
}

configure<ModPublishExtension> {
    changelog = providers.environmentVariable("CHANGELOG")
        .map { it.trim() }
        .getOrElse("No changelog provided")
    type = if (rootProject.ext["snapshot"] == true) BETA else STABLE
    dryRun = !hasProperty("noDryPublish")

    // if there is no publishing platform set, assume this is the root task
    // which is used to support github releases properly
    if (!hasProperty("publishing.platform")) {
        github {
            accessToken = providers.environmentVariable("GITHUB_API_TOKEN")
            repository = providers.environmentVariable("GITHUB_REPOSITORY")
            commitish = providers.environmentVariable("GITHUB_REF_NAME")
            tagName = version.map { "v${it}" }
            displayName = version

            // won't be empty, just so the publishing plugin won't complain
            allowEmptyFiles = true
        }

        // iterate through all subprojects and search for configured release files
        subprojects.filter { it.hasProperty("publishing.platform") }.forEach {
            // add main platform publishing file after project has finished evaluating
            it.afterEvaluate {
                additionalFiles.from(publishMods.file)
            }
        }
    } else {
        if (!hasProperty("publishing.skip_files")) {
            // setup files for this platform
            file = tasks.named<ShadowJar>("shadowJar").flatMap { it.archiveFile }
            additionalFiles.from(tasks.named<Jar>("sourcesJar").flatMap { it.archiveFile })
        }

        val platform = property("publishing.platform").toString()
        val fancyPlatform = platform.replaceFirstChar { it.titlecaseChar() }

        // setup per-platform modrinth publishing
        modrinth {
            accessToken = providers.environmentVariable("MODRINTH_API_TOKEN")
            var versionBob = "${rootProject.ext["fullVersion"]}+${platform}"
            if (rootProject.ext["snapshot"] == true) {
                // e.g. "0.0.0+platform.abcdefg"
                version = "${versionBob}.${rootProject.ext["commitHash"]}"
            } else {
                // e.g. "0.0.0+platform"
                version = versionBob
            }
            displayName = "${rootProject.ext["publishing.display_name"]} ${rootProject.version} ${fancyPlatform}"
            projectId = property("publishing.modrinth.project").toString()
            minecraftVersionRange {
                start = property("publishing.modrinth.version.start").toString()
                end = property("publishing.modrinth.version.end").toString()
            }
            val loadersStr = property("publishing.modrinth.loaders").toString()
            modLoaders.addAll(loadersStr.split(","))
        }
    }
}

if (hasProperty("publishing.platform")
    && !hasProperty("publishing.skip_files")
) {
    tasks.withType<PublishModTask> {
        dependsOn(tasks.named<ShadowJar>("shadowJar"))
        dependsOn(tasks.named<Jar>("sourcesJar"))
    }
}