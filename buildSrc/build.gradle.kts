plugins {
    `kotlin-dsl`
}

kotlin {
    compilerOptions {
        jvmToolchain(21)
    }
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    maven("https://repo.viaversion.com/everything/")
    maven("https://maven.fabricmc.net/")
}

dependencies {
    implementation(libs.shadow)
    compileOnly(libs.jetbrains.annotations)
    implementation(libs.via.nbt)
    implementation(libs.fast.util)
    implementation(libs.gson)
    implementation(libs.java.diff.utils)
    implementation(libs.mod.publish)
    implementation(libs.fabric.loom)

    // downgrade jackson module for publishing plugin to work
    implementation(libs.runtask) {
        exclude("com.fasterxml.jackson.module", "jackson-module-kotlin")
    }
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.19.2")
}
