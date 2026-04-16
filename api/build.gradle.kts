import com.github.retrooper.compression.strategy.dir.JsonBase64DataDirStrategy
import com.github.retrooper.compression.strategy.dir.JsonRegistryCompressionDirStrategy
import com.github.retrooper.compression.strategy.dir.JsonToNbtDirStrategy
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    packetevents.`shadow-conventions`
    packetevents.`library-conventions`
    `mapping-compression`
    `pe-version`
}

val include: Configuration by configurations.creating {
    configurations.compileOnlyApi.get().extendsFrom(this)
}

// papermc repo + disableAutoTargetJvm needed for mockbukkit
repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    include(libs.bundles.adventure)
    include(libs.bundles.adventure.serializers) {
        exclude(module = "gson")
    }

    compileOnly(libs.gson)
    compileOnly(libs.checkerqual)

    testImplementation(libs.bundles.adventure)
    testImplementation(libs.bundles.adventure.serializers)

    testImplementation(project(":netty-common"))
    testImplementation(testlibs.slf4j)
    testImplementation(libs.netty)
    testImplementation(libs.classgraph)

    testImplementation(project(":spigot"))
    testImplementation(testlibs.mockbukkit)
    testImplementation(testlibs.paper.api)

    testImplementation(testlibs.bundles.junit)
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.11.2")
}

mappingCompression {
    mappingDirectory = rootDir.resolve("mappings")
    outDirectory = project.layout.buildDirectory.dir("mappings/generated/assets/mappings")

    with<JsonToNbtDirStrategy> {
        compress("data")
    }
    with<JsonBase64DataDirStrategy> {
        compress("item_base_components")
    }
    with<JsonRegistryCompressionDirStrategy> {
        compress("registries")
    }
}

tasks {
    javadoc {
        val options = options as StandardJavadocDocletOptions
        options.use(true)
        options.tags("versions:A:Minecraft Versions:")
        mustRunAfter(generateVersionsFile)
    }

    sourcesJar {
        mustRunAfter(generateVersionsFile)
    }

    withType<JavaCompile> {
        dependsOn(generateVersionsFile)
    }

    // TODO only include where needed (spigot/paper + bungee?)
    // TODO do we even want to include this? maybe just download at runtime because of version conflicts anyway?
    named<Jar>("jar") {
        from(include.resolve()) {
            rename { it.replace(Regex("^([\\w\\-]+)-(?:\\d+\\.){2,3}jar$"), "assets/libs/$1.jar") }
        }
    }

    processResources {
        dependsOn(compressMappings)
        from(project.layout.buildDirectory.dir("mappings/generated").get())
    }

    generateVersionsFile {
        packageName = "com.github.retrooper.packetevents.util"
    }

    test {
        useJUnitPlatform()
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
        }
    }
}
