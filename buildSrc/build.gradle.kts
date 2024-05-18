plugins {
    java
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    maven("https://repo.viaversion.com")
}

dependencies {
    implementation("com.github.johnrengelman:shadow:8.1.1")
    compileOnly("org.jetbrains:annotations:23.0.0")
    implementation("com.viaversion:nbt:4.0.0")
    implementation("it.unimi.dsi:fastutil:8.5.6")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("io.github.java-diff-utils:java-diff-utils:4.12")
}