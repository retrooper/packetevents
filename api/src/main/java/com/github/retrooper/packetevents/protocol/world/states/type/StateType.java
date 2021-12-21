package com.github.retrooper.packetevents.protocol.world.states.type;

import com.github.retrooper.packetevents.protocol.world.MaterialType;
import com.github.retrooper.packetevents.protocol.world.PushReaction;

public class StateType {
    private final String name;
    private final float blastResistance;
    private final float hardness;
    private final float slipperiness;
    private final float friction;
    private final float speed;
    private final boolean hasGravity;
    private final boolean isAir;
    private final boolean isBurnable;
    private final boolean isFlammable;
    private final boolean isOccluding;
    private final boolean isSolid;
    private final boolean isLiquid;
    private final boolean isBlocking;
    private final boolean requiresCorrectTool;
    private final boolean isReplaceable;
    private final boolean exceedsCube;
    private final PushReaction pushReaction;
    private final MaterialType materialType;

    public StateType(String name, float blastResistance, float hardness, float slipperiness, float friction, float speed, boolean hasGravity,
                     boolean isAir, boolean isBurnable, boolean isFlammable, boolean isOccluding, boolean isSolid, boolean isLiquid,
                     boolean isBlocking, boolean requiresCorrectTool, boolean isReplaceable, boolean exceedsCube, PushReaction pushReaction, MaterialType materialType) {
        this.name = name;
        this.blastResistance = blastResistance;
        this.hardness = hardness;
        this.slipperiness = slipperiness;
        this.friction = friction;
        this.speed = speed;
        this.hasGravity = hasGravity;
        this.isAir = isAir;
        this.isBurnable = isBurnable;
        this.isFlammable = isFlammable;
        this.isOccluding = isOccluding;
        this.isSolid = isSolid;
        this.isLiquid = isLiquid;
        this.isBlocking = isBlocking;
        this.requiresCorrectTool = requiresCorrectTool;
        this.isReplaceable = isReplaceable;
        this.exceedsCube = exceedsCube;
        this.pushReaction = pushReaction;
        this.materialType = materialType;
    }

    public String getName() {
        return name;
    }

    public float getBlastResistance() {
        return blastResistance;
    }

    public float getHardness() {
        return hardness;
    }

    public float getSlipperiness() {
        return slipperiness;
    }

    public float getFriction() {
        return friction;
    }

    public float getSpeed() {
        return speed;
    }

    public boolean isHasGravity() {
        return hasGravity;
    }

    public boolean isAir() {
        return isAir;
    }

    public boolean isBurnable() {
        return isBurnable;
    }

    public boolean isFlammable() {
        return isFlammable;
    }

    public boolean isOccluding() {
        return isOccluding;
    }

    public boolean isSolid() {
        return isSolid;
    }

    public boolean isLiquid() {
        return isLiquid;
    }

    public boolean isBlocking() {
        return isBlocking;
    }

    public boolean isRequiresCorrectTool() {
        return requiresCorrectTool;
    }

    public boolean isReplaceable() {
        return isReplaceable;
    }

    public boolean exceedsCube() {
        return exceedsCube;
    }

    public PushReaction getPushReaction() {
        return pushReaction;
    }

    public MaterialType getMaterialType() {
        return materialType;
    }
}
