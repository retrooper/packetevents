/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.retrooper.packetevents.protocol.npc;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.player.*;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class NPC {
    private final int id;
    private final UserProfile profile;
    private Component tabName;
    private NamedTextColor nameColor;
    private Component prefixName;
    private Component suffixName;
    private int displayPing = 0;
    private Location location = new Location(0.0, 0.0, 0.0, 0.0f, 0.0f);
    private ItemStack mainHand = null;
    private ItemStack offHand = null;
    private ItemStack helmet = null;
    private ItemStack chestPlate = null;
    private ItemStack leggings = null;
    private ItemStack boots = null;
    private final Set<Object> channels = new HashSet<>();

    public NPC(UserProfile profile, int entityId, @Nullable Component tabName, @Nullable NamedTextColor nameColor,
               @Nullable Component prefixName, @Nullable Component suffixName) {
        this.profile = profile;
        this.id = entityId;

        this.tabName = tabName;
        this.nameColor = nameColor;
        this.prefixName = prefixName;
        this.suffixName = suffixName;
    }

    public NPC(UserProfile profile, int entityId, @Nullable Component tabName) {
        this(profile, entityId, tabName, null, null, null);
    }

    public NPC(UserProfile profile, int entityId) {
        this(profile, entityId, null);
    }

    public boolean hasSpawned(Object channel) {
        return channels.contains(channel);
    }

    public void spawn(Object channel) {
        if (hasSpawned(channel)) return;
        WrapperPlayServerPlayerInfo playerInfoPacket =
                new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.ADD_PLAYER, getPlayerInfoData());
        PacketEvents.getAPI().getProtocolManager().sendPacket(channel, playerInfoPacket);

        //TODO Later if we want entity metadata, its not supported on newer server versions though(confirm if its mandatory on older versions)
        WrapperPlayServerSpawnPlayer spawnPlayer = new WrapperPlayServerSpawnPlayer(getId(),
                getProfile().getUUID(),
                getLocation());
        PacketEvents.getAPI().getProtocolManager().sendPacket(channel, spawnPlayer);

        //Create team
        if (getNameColor() != null || getPrefixName() != null
                || getSuffixName() != null) {
            PacketEvents.getAPI().getProtocolManager().sendPacket(channel, generateTeamsData());
        }
        channels.add(channel);
    }

    public void despawn(Object channel) {
        if (!hasSpawned(channel)) return;
        //TODO Confirm if we need to destroy the team too
        WrapperPlayServerDestroyEntities destroyEntities = new WrapperPlayServerDestroyEntities(getId());
        PacketEvents.getAPI().getProtocolManager().sendPacket(channel, destroyEntities);
        channels.remove(channel);
    }

    public void despawnAll() {
        for (Object channel : channels) {
            WrapperPlayServerDestroyEntities destroyEntities = new WrapperPlayServerDestroyEntities(getId());
            PacketEvents.getAPI().getProtocolManager().sendPacket(channel, destroyEntities);
        }
    }

    public void teleport(Location to) {
        setLocation(to);
        for (Object channel : channels) {
            WrapperPlayServerEntityTeleport entityTeleport = new WrapperPlayServerEntityTeleport(getId(), to, true);
            PacketEvents.getAPI().getProtocolManager().sendPacket(channel, entityTeleport);
        }
    }

    public void updateLocation(Location to) {
        Location from = getLocation();
        setLocation(to);
        double distXAbs = Math.abs(to.getPosition().getX() - from.getPosition().getX());
        double distYAbs = Math.abs(to.getPosition().getY() - from.getPosition().getY());
        double distZAbs = Math.abs(to.getPosition().getZ() - from.getPosition().getZ());
        boolean shouldUseEntityTeleport = distXAbs > 8 ||
                distYAbs > 8 ||
                distZAbs > 8;
        for (Object channel : channels) {
            if (shouldUseEntityTeleport) {
                WrapperPlayServerEntityTeleport entityTeleport =
                        new WrapperPlayServerEntityTeleport(getId(), to, true);
                PacketEvents.getAPI().getProtocolManager().sendPacket(channel, entityTeleport);
            } else {
                boolean rotationChanged =
                        to.getYaw() != from.getYaw() || to.getPitch() != from.getPitch();
                boolean positionChanged =
                        to.getPosition().getX() != from.getPosition().getX() ||
                                to.getPosition().getY() != from.getPosition().getY() ||
                                to.getPosition().getZ() != from.getPosition().getZ();
                double deltaX = positionChanged ? (to.getPosition().getX() - from.getPosition().getX()) : 0;
                double deltaY = positionChanged ? (to.getPosition().getY() - from.getPosition().getY()) : 0;
                double deltaZ = positionChanged ? (to.getPosition().getZ() - from.getPosition().getZ()) : 0;
                if (positionChanged && rotationChanged) {
                    WrapperPlayServerEntityRelativeMoveAndRotation entityRelativeMoveAndRotation =
                            new WrapperPlayServerEntityRelativeMoveAndRotation(getId(), deltaX, deltaY, deltaZ,
                                    to.getYaw(), to.getPitch(), true);
                    PacketEvents.getAPI().getProtocolManager()
                            .sendPacket(channel, entityRelativeMoveAndRotation);

                    WrapperPlayServerEntityHeadLook headYaw =
                            new WrapperPlayServerEntityHeadLook(getId(), to.getYaw());
                    PacketEvents.getAPI().getProtocolManager().sendPacket(channel, headYaw);
                } else if (positionChanged) {
                    WrapperPlayServerEntityRelativeMove entityRelativeMove =
                            new WrapperPlayServerEntityRelativeMove(getId(), deltaX, deltaY, deltaZ, true);
                    PacketEvents.getAPI().getProtocolManager().sendPacket(channel, entityRelativeMove);
                } else if (rotationChanged) {
                    WrapperPlayServerEntityRotation entityRotation =
                            new WrapperPlayServerEntityRotation(getId(), to.getYaw(), to.getPitch(), true);
                    PacketEvents.getAPI().getProtocolManager().sendPacket(channel, entityRotation);

                    WrapperPlayServerEntityHeadLook headYaw =
                            new WrapperPlayServerEntityHeadLook(getId(), to.getYaw());
                    PacketEvents.getAPI().getProtocolManager().sendPacket(channel, headYaw);
                }
            }
        }
    }

    public void updateRotation(float yaw, float pitch) {
        getLocation().setYaw(yaw);
        getLocation().setPitch(pitch);
        for (Object channel : channels) {
            WrapperPlayServerEntityRotation entityRotation =
                    new WrapperPlayServerEntityRotation(getId(), yaw, pitch, true);
            PacketEvents.getAPI().getProtocolManager().sendPacket(channel, entityRotation);

            WrapperPlayServerEntityHeadLook headYaw =
                    new WrapperPlayServerEntityHeadLook(getId(), yaw);
            PacketEvents.getAPI().getProtocolManager().sendPacket(channel, headYaw);
        }
    }

    public void updateTabPing(int ping) {
        setDisplayPing(ping);
        for (Object channel : channels) {
            WrapperPlayServerPlayerInfo playerInfo =
                    new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.UPDATE_LATENCY, getPlayerInfoData());
            PacketEvents.getAPI().getProtocolManager().sendPacket(channel, playerInfo);
        }
    }

    public void changeSkin(UUID skinUUID, List<TextureProperty> skinTextureProperties) {
        for (Object channel : channels) {
            WrapperPlayServerPlayerInfo playerInfoRemove =
                    new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.REMOVE_PLAYER, getPlayerInfoData());
            PacketEvents.getAPI().getProtocolManager().sendPacket(channel, playerInfoRemove);

            WrapperPlayServerDestroyEntities destroyEntities =
                    new WrapperPlayServerDestroyEntities(getId());
            PacketEvents.getAPI().getProtocolManager().sendPacket(channel, destroyEntities);

            getProfile().setTextureProperties(skinTextureProperties);
            getProfile().setUUID(skinUUID);
            WrapperPlayServerPlayerInfo playerInfoAdd =
                    new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.ADD_PLAYER, getPlayerInfoData());
            PacketEvents.getAPI().getProtocolManager().sendPacket(channel, playerInfoAdd);

            WrapperPlayServerSpawnPlayer spawnPlayer =
                    new WrapperPlayServerSpawnPlayer(getId(), getProfile().getUUID(), getLocation());
            PacketEvents.getAPI().getProtocolManager().sendPacket(channel, spawnPlayer);
        }
    }

    public void updateNameTag(NPC npc) {
        for (Object channel : channels) {
            //Destroy team
            WrapperPlayServerTeams removeTeam =
                    new WrapperPlayServerTeams("custom_name_team",
                            WrapperPlayServerTeams.TeamMode.REMOVE,
                            Optional.empty());
            PacketEvents.getAPI().getProtocolManager().sendPacket(channel, removeTeam);

            if (npc.getNameColor() != null || npc.getPrefixName() != null
                    || npc.getSuffixName() != null) {
                PacketEvents.getAPI().getProtocolManager().sendPacket(channel, generateTeamsData());
            }
        }
    }

    public void updateEquipment() {
        for (Object channel : channels) {
            List<Equipment> equipmentList = new ArrayList<>();
            ItemStack handItem = getMainHand();
            if (handItem == null) {
                handItem = ItemStack.EMPTY;
            }
            equipmentList.add(new Equipment(EquipmentSlot.MAINHAND,
                    handItem));
            if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
                ItemStack offHandItem = getOffHand();
                if (offHandItem == null) {
                    offHandItem = ItemStack.EMPTY;
                }
                equipmentList.add(new Equipment(EquipmentSlot.OFFHAND,
                        offHandItem));
            }
            ItemStack helmetItem = getHelmet();
            if (helmetItem == null) {
                helmetItem = ItemStack.EMPTY;
            }
            equipmentList.add(new Equipment(EquipmentSlot.HELMET,
                    helmetItem));

            ItemStack chestPlateItem = getChestplate();
            if (chestPlateItem == null) {
                chestPlateItem = ItemStack.EMPTY;
            }
            equipmentList.add(new Equipment(EquipmentSlot.CHESTPLATE,
                    chestPlateItem));
            ItemStack leggingsItem = getLeggings();
            if (leggingsItem == null) {
                leggingsItem = ItemStack.EMPTY;
            }
            equipmentList.add(new Equipment(EquipmentSlot.LEGGINGS,
                    leggingsItem));
            ItemStack bootsItem = getBoots();
            if (bootsItem == null) {
                bootsItem = ItemStack.EMPTY;
            }
            equipmentList.add(new Equipment(EquipmentSlot.BOOTS,
                    bootsItem));

            WrapperPlayServerEntityEquipment equipmentPacket
                    = new WrapperPlayServerEntityEquipment(getId(),
                    equipmentList);
            PacketEvents.getAPI().getProtocolManager().sendPacket(channel, equipmentPacket);
        }
    }

    public ItemStack getMainHand() {
        return mainHand;
    }

    public void setMainHand(ItemStack mainHand) {
        this.mainHand = mainHand;
    }

    public ItemStack getOffHand() {
        return offHand;
    }

    public void setOffHand(ItemStack offHand) {
        this.offHand = offHand;
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;
    }

    public ItemStack getChestplate() {
        return chestPlate;
    }

    public void setChestplate(ItemStack chestPlate) {
        this.chestPlate = chestPlate;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public void setLeggings(ItemStack leggings) {
        this.leggings = leggings;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public void setBoots(ItemStack boots) {
        this.boots = boots;
    }

    @Nullable
    public NamedTextColor getNameColor() {
        return nameColor;
    }

    public void setNameColor(@Nullable NamedTextColor nameColor) {
        this.nameColor = nameColor;
    }

    @Nullable
    public Component getPrefixName() {
        return prefixName;
    }

    public void setPrefixName(@Nullable Component namePrefix) {
        this.prefixName = namePrefix;
    }

    @Nullable
    public Component getSuffixName() {
        return suffixName;
    }

    public void setSuffixName(@Nullable Component nameSuffix) {
        this.suffixName = nameSuffix;
    }

    @Nullable
    public Component getTabName() {
        return tabName;
    }

    public void setTabName(@Nullable Component tabName) {
        this.tabName = tabName;
    }

    public int getId() {
        return id;
    }

    public UserProfile getProfile() {
        return profile;
    }

    private WrapperPlayServerTeams generateTeamsData() {
        return new WrapperPlayServerTeams("custom_name_team",
                WrapperPlayServerTeams.TeamMode.CREATE,
                Optional.of(
                        new WrapperPlayServerTeams.ScoreBoardTeamInfo(
                                Component.text("custom_name_team"),
                                prefixName,
                                suffixName,
                                WrapperPlayServerTeams.NameTagVisibility.ALWAYS,
                                WrapperPlayServerTeams.CollisionRule.ALWAYS,
                                nameColor,
                                WrapperPlayServerTeams.OptionData.NONE
                        )),
                getProfile().getName());
    }

    public WrapperPlayServerPlayerInfo.PlayerData getPlayerInfoData() {
        return new WrapperPlayServerPlayerInfo.PlayerData(getTabName(),
                getProfile(), GameMode.SURVIVAL,
                getDisplayPing());
    }

    public int getDisplayPing() {
        return displayPing;
    }

    public void setDisplayPing(int ping) {
        this.displayPing = ping;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
