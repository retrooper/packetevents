package com.github.retrooper

import org.gradle.api.artifacts.ModuleDependency
import org.gradle.kotlin.dsl.exclude

fun ModuleDependency.excludeAdventure() {
    exclude(group = "net.kyori", module = "adventure-api")
    exclude(group = "net.kyori", module = "adventure-nbt")
    exclude(group = "net.kyori", module = "examination-api")
    exclude(group = "net.kyori", module = "examination-string")
}

