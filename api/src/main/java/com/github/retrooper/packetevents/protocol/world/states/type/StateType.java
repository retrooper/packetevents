package com.github.retrooper.packetevents.protocol.world.states.type;

import com.github.retrooper.packetevents.protocol.world.MaterialType;
import com.github.retrooper.packetevents.protocol.world.PushReaction;

public class StateType {
    String name;
    float blastResistance;
    float hardness;
    float slipperiness;
    float friction;
    float speed;
    boolean hasGravity;
    boolean isAir;
    boolean isBurnable;
    boolean isFlammable;
    boolean isOccluding;
    boolean isSolid;
    boolean isLiquid;
    boolean isBlocking;
    boolean requiresCorrectTool;
    boolean isReplaceable;
    PushReaction pushReaction;
    MaterialType materialType;

    // Reduce the number of string comparisons required for using block data strings
    private int optimization_internal_start_id;
    private int optimization_internal_end_id;

    public StateType(String name, float blastResistance, float hardness, float slipperiness, float friction, float speed, boolean hasGravity,
                     boolean isAir, boolean isBurnable, boolean isFlammable, boolean isOccluding, boolean isSolid, boolean isLiquid,
                     boolean isBlocking, boolean requiresCorrectTool, boolean isReplaceable, PushReaction pushReaction, MaterialType materialType) {
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
        this.pushReaction = pushReaction;
        this.materialType = materialType;
    }

    public void updateInternalBlockRepresentationsOptimization(int position) {
        optimization_internal_start_id = Math.min(optimization_internal_end_id, position);
        optimization_internal_end_id = Math.max(optimization_internal_end_id, position);
    }
}
