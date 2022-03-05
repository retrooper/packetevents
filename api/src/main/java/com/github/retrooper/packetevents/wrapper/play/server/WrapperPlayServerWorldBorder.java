package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

// Thanks MCProtocolLib, this is taken entirely from them
public class WrapperPlayServerWorldBorder extends PacketWrapper<WrapperPlayServerWorldBorder> {
    private WorldBorderAction action;

    private double radius;

    private double oldRadius;
    private double newRadius;
    private long speed;

    private double centerX;
    private double centerY;

    private int portalTeleportBoundary;

    private int warningTime;

    private int warningBlocks;

    public WrapperPlayServerWorldBorder(double radius) {
        super(PacketType.Play.Server.WORLD_BORDER);
        this.action = WorldBorderAction.SET_SIZE;
        this.radius = radius;
    }

    public WrapperPlayServerWorldBorder(double oldRadius, double newRadius, long speed) {
        super(PacketType.Play.Server.WORLD_BORDER);
        this.action = WorldBorderAction.LERP_SIZE;
        this.oldRadius = oldRadius;
        this.newRadius = newRadius;
        this.speed = speed;
    }

    public WrapperPlayServerWorldBorder(double centerX, double centerY) {
        super(PacketType.Play.Server.WORLD_BORDER);
        this.action = WorldBorderAction.SET_CENTER;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public WrapperPlayServerWorldBorder(double centerX, double centerY, double oldRadius, double newRadius, long speed, int portalTeleportBoundary, int warningTime, int warningBlocks) {
        super(PacketType.Play.Server.WORLD_BORDER);
        this.action = WorldBorderAction.INITIALIZE;
        this.centerX = centerX;
        this.centerY = centerY;
        this.oldRadius = oldRadius;
        this.newRadius = newRadius;
        this.speed = speed;
        this.portalTeleportBoundary = portalTeleportBoundary;
        this.warningTime = warningTime;
        this.warningBlocks = warningBlocks;
    }

    public WrapperPlayServerWorldBorder(int warning, boolean time) {
        super(PacketType.Play.Server.WORLD_BORDER);
        if (time) {
            this.action = WorldBorderAction.SET_WARNING_TIME;
            this.warningTime = warning;
        } else {
            this.action = WorldBorderAction.SET_WARNING_BLOCKS;
            this.warningBlocks = warning;
        }
    }

    public WorldBorderAction getAction() {
        return this.action;
    }

    public double getRadius() {
        return this.radius;
    }

    public double getOldRadius() {
        return this.oldRadius;
    }

    public double getNewRadius() {
        return this.newRadius;
    }

    public long getSpeed() {
        return this.speed;
    }

    public double getCenterX() {
        return this.centerX;
    }

    public double getCenterY() {
        return this.centerY;
    }

    public int getPortalTeleportBoundary() {
        return this.portalTeleportBoundary;
    }

    public int getWarningTime() {
        return this.warningTime;
    }

    public int getWarningBlocks() {
        return this.warningBlocks;
    }

    @Override
    public void readData() {
        this.action = WorldBorderAction.values()[this.readVarInt()];
        if (this.action == WorldBorderAction.SET_SIZE) {
            this.radius = readDouble();
        } else if (this.action == WorldBorderAction.LERP_SIZE) {
            this.oldRadius = readDouble();
            this.newRadius = readDouble();
            this.speed = readVarLong();
        } else if (this.action == WorldBorderAction.SET_CENTER) {
            this.centerX = readDouble();
            this.centerY = readDouble();
        } else if (this.action == WorldBorderAction.INITIALIZE) {
            this.centerX = readDouble();
            this.centerY = readDouble();
            this.oldRadius = readDouble();
            this.newRadius = readDouble();
            this.speed = readVarLong();
            this.portalTeleportBoundary = readVarInt();
            this.warningTime = readVarInt();
            this.warningBlocks = readVarInt();
        } else if (this.action == WorldBorderAction.SET_WARNING_TIME) {
            this.warningTime = readVarInt();
        } else if (this.action == WorldBorderAction.SET_WARNING_BLOCKS) {
            this.warningBlocks = readVarInt();
        }
    }

    @Override
    public void readData(WrapperPlayServerWorldBorder wrapper) {
        this.action = wrapper.action;
        this.radius = wrapper.radius;
        this.oldRadius = wrapper.oldRadius;
        this.newRadius = wrapper.newRadius;
        this.speed = wrapper.speed;
        this.centerX = wrapper.centerX;
        this.centerY = wrapper.centerY;
        this.portalTeleportBoundary = wrapper.portalTeleportBoundary;
        this.warningTime = wrapper.warningTime;
        this.warningBlocks = wrapper.warningBlocks;
    }

    @Override
    public void writeData() {
        writeVarInt(this.action.ordinal());
        if (this.action == WorldBorderAction.SET_SIZE) {
            writeDouble(this.radius);
        } else if (this.action == WorldBorderAction.LERP_SIZE) {
            writeDouble(this.oldRadius);
            writeDouble(this.newRadius);
            writeVarLong(this.speed);
        } else if (this.action == WorldBorderAction.SET_CENTER) {
            writeDouble(this.centerX);
            writeDouble(this.centerY);
        } else if (this.action == WorldBorderAction.INITIALIZE) {
            writeDouble(this.centerX);
            writeDouble(this.centerY);
            writeDouble(this.oldRadius);
            writeDouble(this.newRadius);
            writeVarLong(this.speed);
            writeVarInt(this.portalTeleportBoundary);
            writeVarInt(this.warningTime);
            writeVarInt(this.warningBlocks);
        } else if (this.action == WorldBorderAction.SET_WARNING_TIME) {
            writeVarInt(this.warningTime);
        } else if (this.action == WorldBorderAction.SET_WARNING_BLOCKS) {
            writeVarInt(this.warningBlocks);
        }
    }

    public enum WorldBorderAction {
        SET_SIZE,
        LERP_SIZE,
        SET_CENTER,
        INITIALIZE,
        SET_WARNING_TIME,
        SET_WARNING_BLOCKS;
    }
}
