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

package com.github.retrooper.packetevents.manager.npc;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.GameProfile;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class NPC {
    private final int id;
    private final GameProfile profile;
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

    public NPC(GameProfile profile, int entityId, @Nullable Component tabName, @Nullable NamedTextColor nameColor,
               @Nullable Component prefixName, @Nullable Component suffixName) {
        this.profile = profile;
        this.id = entityId;

        this.tabName = tabName;
        this.nameColor = nameColor;
        this.prefixName = prefixName;
        this.suffixName = suffixName;
    }

    public NPC(GameProfile profile, int entityId, @Nullable Component tabName) {
        this(profile, entityId, tabName, null, null, null);
    }

    public NPC(GameProfile profile, int entityId) {
        this(profile, entityId, null);
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

    public GameProfile getProfile() {
        return profile;
    }

    public WrapperPlayServerTeams getTeamData() {
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
