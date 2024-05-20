import com.github.retrooper.compression.strategy.JsonArrayCompressionStrategy
import com.github.retrooper.compression.strategy.JsonObjectCompressionStrategy
import com.github.retrooper.compression.strategy.JsonToNbtStrategy

plugins {
    packetevents.`library-conventions`
    packetevents.`shadow-conventions`
    `mapping-compression`
}

// papermc repo + disableAutoTargetJvm needed for mockbukkit
repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

java {
    withJavadocJar()
}

dependencies {
    compileOnly(libs.bundles.adventure)

    testImplementation(libs.bundles.adventure)
    testImplementation(project(":netty-common"))
    testImplementation(testlibs.mockbukkit)
    testImplementation(testlibs.slf4j)
    testImplementation(testlibs.bundles.junit)
}

mappingCompression {
    mappingDirectory = rootDir.resolve("mappings")
    outDirectory = project.layout.buildDirectory.dir("mappings/generated/assets/mappings")

    with<JsonToNbtStrategy> {
        compress("block/legacy_block_mappings.json")
        compress("block/modern_block_mappings.json")

        compress("stats/statistics.json")
    }

    with<JsonArrayCompressionStrategy> {
        compress("attribute/attribute_mappings.json")

        compress("block/block_entity_type_mappings.json")
        compress("block/block_type_mappings.json")

        compress("chat/chat_type_mappings.json")

        compress("command/argument_parser_mappings.json")

        compress("entity/entity_data_type_mappings.json")

        compress("item/item_armor_material_mappings.json")
        compress("item/item_banner_pattern_mappings.json")
        compress("item/item_component_mappings.json")
        compress("item/item_instrument_mappings.json")
        compress("item/item_map_decoration_type_mappings.json")
        compress("item/item_potion_mappings.json")
        compress("item/item_trim_material_mappings.json")
        compress("item/item_trim_pattern_mappings.json")

        compress("particle/particle_type_mappings.json")

        compress("sound/sound_mappings.json")

        compress("world/world_position_source_mappings.json")
    }

    with<JsonObjectCompressionStrategy> {
        compress("enchantment/enchantment_type_mappings.json")

        compress("entity/entity_effect_mappings.json")
        compress("entity/entity_type_mappings.json")
        compress("entity/legacy_entity_type_mappings.json")

        compress("item/item_type_mappings.json")
    }
}

tasks {
    javadoc {
        setDestinationDir(file("${project.layout.buildDirectory.asFile.get()}/docs/javadoc"))
        options {
            (this as CoreJavadocOptions).addBooleanOption("Xdoclint:none", true)
        }
    }

    processResources {
        dependsOn(compressMappings)
        from(project.layout.buildDirectory.dir("mappings/generated").get())
    }

    test {
        useJUnitPlatform()
    }
}