package com.github.retrooper.packetevents.protocol.entity.sulfurcube;

import com.github.retrooper.packetevents.protocol.attribute.Attribute;
import com.github.retrooper.packetevents.protocol.attribute.AttributeEntry;
import com.github.retrooper.packetevents.protocol.attribute.AttributeModifier;
import com.github.retrooper.packetevents.protocol.attribute.AttributeOperation;
import com.github.retrooper.packetevents.protocol.attribute.Attributes;
import com.github.retrooper.packetevents.protocol.sound.Sounds;
import com.github.retrooper.packetevents.protocol.valueproviders.floats.ConstantFloat;
import com.github.retrooper.packetevents.protocol.world.damagetype.DamageTypes;
import com.github.retrooper.packetevents.protocol.world.states.defaulttags.ItemTags;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import net.kyori.adventure.util.Ticks;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.List;

import static com.github.retrooper.packetevents.protocol.entity.sulfurcube.SulfurCubeArchtypeBuilder.sulfurCubeArchtypeBuilder;

/**
 * @versions 26.2+
 */
@NullMarked
public final class SulfurCubeArchtypes {

    private static final VersionedRegistry<SulfurCubeArchtype> REGISTRY = new VersionedRegistry<>("sulfur_cube_archtype");

    private SulfurCubeArchtypes() {
    }

    private static AttributeEntry createAttributeAdd(String name, Attribute attribute, double value) {
        return createAttribute(name + "_add", attribute, value, AttributeOperation.ADDITION);
    }

    private static AttributeEntry createAttributeMultiply(String name, Attribute attribute, double value) {
        return createAttribute(name + "_mul", attribute, value - 1d, AttributeOperation.MULTIPLY_TOTAL);
    }

    private static AttributeEntry createAttribute(String name, Attribute attribute, double value, AttributeOperation operation) {
        ResourceLocation modifierName = new ResourceLocation(name + "_" + attribute.getName().getKey());
        return new AttributeEntry(attribute, new AttributeModifier(modifierName, value, operation));
    }

    private static List<AttributeEntry> createAttributes(String name, float speed, float bounce, float friction, float drag) {
        return Arrays.asList(
                createAttributeAdd(name, Attributes.KNOCKBACK_RESISTANCE, -speed),
                createAttributeAdd(name, Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, -speed),
                createAttributeAdd(name, Attributes.BOUNCINESS, bounce),
                createAttributeMultiply(name, Attributes.FRICTION_MODIFIER, friction),
                createAttributeMultiply(name, Attributes.AIR_DRAG_MODIFIER, drag)
        );
    }

    public static final SulfurCubeArchtype BOUNCY = REGISTRY.defineWithBuilder("bouncy", sulfurCubeArchtypeBuilder()
            .setItems(ItemTags.SULFUR_CUBE_ARCHETYPE_BOUNCY)
            .setAttributeModifiers(createAttributes("bouncy", 2f, 0.9f, 0.3f, 0.01f))
            .setBuoyant(true)
            .setKnockbackModifiers(new SulfurCubeKnockbackModifiers(0.4125f, 0.105f))
            .setSoundSettings(new SulfurCubeSoundSettings(Sounds.ENTITY_SULFUR_CUBE_BOUNCY_HIT, Sounds.ENTITY_SULFUR_CUBE_BOUNCY_PUSH, 0.3f, 0.7f)));
    public static final SulfurCubeArchtype EXPLOSIVE = REGISTRY.defineWithBuilder("explosive", sulfurCubeArchtypeBuilder()
            .setItems(ItemTags.SULFUR_CUBE_ARCHETYPE_EXPLOSIVE)
            .setAttributeModifiers(createAttributes("explosive", 1f, 0.5f, 0.3f, 0.3f))
            .setBuoyant(true)
            .setExplosion(new SulfurCubeExplosionData(3, false, Ticks.TICKS_PER_SECOND * 6))
            .setKnockbackModifiers(new SulfurCubeKnockbackModifiers(0.4125f, 0.09f))
            .setSoundSettings(new SulfurCubeSoundSettings(Sounds.ENTITY_SULFUR_CUBE_EXPLOSIVE_HIT, Sounds.ENTITY_SULFUR_CUBE_EXPLOSIVE_PUSH, 0.1f, 0.7f)));
    public static final SulfurCubeArchtype FAST_FLAT = REGISTRY.defineWithBuilder("fast_flat", sulfurCubeArchtypeBuilder()
            .setItems(ItemTags.SULFUR_CUBE_ARCHETYPE_FAST_FLAT)
            .setAttributeModifiers(createAttributes("fast_flat", 1f, 0.5f, 0.2f, 0.01f))
            .setBuoyant(false)
            .setKnockbackModifiers(new SulfurCubeKnockbackModifiers(0.9125f, 0.09f))
            .setSoundSettings(new SulfurCubeSoundSettings(Sounds.ENTITY_SULFUR_CUBE_FAST_FLAT_HIT, Sounds.ENTITY_SULFUR_CUBE_FAST_FLAT_PUSH, 0.03f, 0.9f)));
    public static final SulfurCubeArchtype FAST_SLIDING = REGISTRY.defineWithBuilder("fast_sliding", sulfurCubeArchtypeBuilder()
            .setItems(ItemTags.SULFUR_CUBE_ARCHETYPE_FAST_SLIDING)
            .setAttributeModifiers(createAttributes("fast_sliding", -0.5f, 0.1f, 0.05f, 0.01f))
            .setBuoyant(false)
            .setKnockbackModifiers(new SulfurCubeKnockbackModifiers(0.6625f, 0.09f))
            .setSoundSettings(new SulfurCubeSoundSettings(Sounds.ENTITY_SULFUR_CUBE_FAST_SLIDING_HIT, Sounds.ENTITY_SULFUR_CUBE_FAST_SLIDING_PUSH, 0.05f, 1f)));
    public static final SulfurCubeArchtype HIGH_RESISTANCE = REGISTRY.defineWithBuilder("high_resistance", sulfurCubeArchtypeBuilder()
            .setItems(ItemTags.SULFUR_CUBE_ARCHETYPE_HIGH_RESISTANCE)
            .setAttributeModifiers(createAttributes("high_resistance", -0.7f, 0.2f, 1f, 0.01f))
            .setBuoyant(false)
            .setKnockbackModifiers(new SulfurCubeKnockbackModifiers(0.4125f, 0.09f))
            .setSoundSettings(new SulfurCubeSoundSettings(Sounds.ENTITY_SULFUR_CUBE_HIGH_RESISTANCE_HIT, Sounds.ENTITY_SULFUR_CUBE_HIGH_RESISTANCE_PUSH, 0.03f, 0.7f)));
    public static final SulfurCubeArchtype HOT = REGISTRY.defineWithBuilder("hot", sulfurCubeArchtypeBuilder()
            .setItems(ItemTags.SULFUR_CUBE_ARCHETYPE_HOT)
            .setAttributeModifiers(createAttributes("hot", 1f, 0.5f, 0.3f, 0.1f))
            .setBuoyant(true)
            .setContactDamage(new SulfurCubeContactDamage(DamageTypes.SULFUR_CUBE_HOT, new ConstantFloat(1f), false))
            .setKnockbackModifiers(new SulfurCubeKnockbackModifiers(0.4125f, 0.09f))
            .setSoundSettings(new SulfurCubeSoundSettings(Sounds.ENTITY_SULFUR_CUBE_HOT_HIT, Sounds.ENTITY_SULFUR_CUBE_HOT_PUSH, 0.2f, 0.7f)));
    public static final SulfurCubeArchtype LIGHT = REGISTRY.defineWithBuilder("light", sulfurCubeArchtypeBuilder()
            .setItems(ItemTags.SULFUR_CUBE_ARCHETYPE_LIGHT)
            .setAttributeModifiers(createAttributes("light", 1f, 1f, 0.3f, 1.8f))
            .setBuoyant(true)
            .setKnockbackModifiers(new SulfurCubeKnockbackModifiers(0.4125f, 0.18f))
            .setSoundSettings(new SulfurCubeSoundSettings(Sounds.ENTITY_SULFUR_CUBE_LIGHT_HIT, Sounds.ENTITY_SULFUR_CUBE_LIGHT_PUSH, 0.2f, 0.7f)));
    public static final SulfurCubeArchtype REGULAR = REGISTRY.defineWithBuilder("regular", sulfurCubeArchtypeBuilder()
            .setItems(ItemTags.SULFUR_CUBE_ARCHETYPE_REGULAR)
            .setAttributeModifiers(createAttributes("regular", 1f, 0.5f, 0.3f, 0.1f))
            .setBuoyant(true)
            .setKnockbackModifiers(new SulfurCubeKnockbackModifiers(0.4125f, 0.09f))
            .setSoundSettings(new SulfurCubeSoundSettings(Sounds.ENTITY_SULFUR_CUBE_REGULAR_HIT, Sounds.ENTITY_SULFUR_CUBE_REGULAR_PUSH, 0.2f, 0.5f)));
    public static final SulfurCubeArchtype SLOW_BOUNCY = REGISTRY.defineWithBuilder("slow_bouncy", sulfurCubeArchtypeBuilder()
            .setItems(ItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_BOUNCY)
            .setAttributeModifiers(createAttributes("slow_bouncy", -0.4f, 0.6f, 0.3f, 0.05f))
            .setBuoyant(false)
            .setKnockbackModifiers(new SulfurCubeKnockbackModifiers(0.4125f, 0.24f))
            .setSoundSettings(new SulfurCubeSoundSettings(Sounds.ENTITY_SULFUR_CUBE_SLOW_BOUNCY_HIT, Sounds.ENTITY_SULFUR_CUBE_SLOW_BOUNCY_PUSH, 0.05f, 0.5f)));
    public static final SulfurCubeArchtype SLOW_FLAT = REGISTRY.defineWithBuilder("slow_flat", sulfurCubeArchtypeBuilder()
            .setItems(ItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_FLAT)
            .setAttributeModifiers(createAttributes("slow_flat", -0.5f, 0.4f, 0.4f, 0.1f))
            .setBuoyant(false)
            .setKnockbackModifiers(new SulfurCubeKnockbackModifiers(0.4125f, 0.105f))
            .setSoundSettings(new SulfurCubeSoundSettings(Sounds.ENTITY_SULFUR_CUBE_SLOW_FLAT_HIT, Sounds.ENTITY_SULFUR_CUBE_SLOW_FLAT_PUSH, 0.03f, 0.9f)));
    public static final SulfurCubeArchtype SLOW_SLIDING = REGISTRY.defineWithBuilder("slow_sliding", sulfurCubeArchtypeBuilder()
            .setItems(ItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_SLIDING)
            .setAttributeModifiers(createAttributes("slow_sliding", -0.8f, 0.1f, 0.05f, 0.01f))
            .setBuoyant(false)
            .setKnockbackModifiers(new SulfurCubeKnockbackModifiers(0.4125f, 0.09f))
            .setSoundSettings(new SulfurCubeSoundSettings(Sounds.ENTITY_SULFUR_CUBE_SLOW_SLIDING_HIT, Sounds.ENTITY_SULFUR_CUBE_SLOW_SLIDING_PUSH, 0.02f, 1f)));
    public static final SulfurCubeArchtype STICKY = REGISTRY.defineWithBuilder("sticky", sulfurCubeArchtypeBuilder()
            .setItems(ItemTags.SULFUR_CUBE_ARCHETYPE_STICKY)
            .setAttributeModifiers(createAttributes("sticky", 2f, 0f, 2f, 0.01f))
            .setBuoyant(false)
            .setKnockbackModifiers(new SulfurCubeKnockbackModifiers(0.4125f, 0.09f))
            .setSoundSettings(new SulfurCubeSoundSettings(Sounds.ENTITY_SULFUR_CUBE_STICKY_HIT, Sounds.ENTITY_SULFUR_CUBE_STICKY_PUSH, 0.05f, 0.5f)));

    public static VersionedRegistry<SulfurCubeArchtype> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}
