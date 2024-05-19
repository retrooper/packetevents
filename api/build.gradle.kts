import com.github.retrooper.MappingCompressionTask
import kotlin.io.path.exists

plugins {
    packetevents.`library-conventions`
    packetevents.`shadow-conventions`
}

// papermc repo + disableAutoTargetJvm needed for mockbukkit
repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

java {
    withJavadocJar()
    disableAutoTargetJvm()
}

dependencies {
    compileOnly(libs.bundles.adventure)

    testImplementation(libs.bundles.adventure)
    testImplementation(project(":netty-common"))
    testImplementation(testlibs.mockbukkit)
    testImplementation(testlibs.slf4j)
    testImplementation(testlibs.bundles.junit)
}

tasks {
    javadoc {
        setDestinationDir(file("${project.layout.buildDirectory.asFile.get()}/docs/javadoc"))
    }

    register<MappingCompressionTask>("generateMapping") {
        mappingsDir = rootDir.toPath().resolve("mappings")
        outDir = project.layout.buildDirectory.dir("mappings/generated/assets/mappings").get().asFile.toPath()
        outputs.upToDateWhen {
            if (!outDir.exists())
                return@upToDateWhen false

            val genModified = outDir.toFile().walk().maxOf { it.lastModified() }
            mappingsDir.toFile().walk().asSequence()
                .filter { it.isFile }
                .any { it.lastModified() > genModified }
        }
    }

    processResources {
        dependsOn("generateMapping")
        from(project.layout.buildDirectory.dir("mappings/generated").get())
    }

    test {
        useJUnitPlatform()
    }
}