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

package io.github.retrooper.packetevents.packetwrappers.play.out.scoreboardteam;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.ChatColor;

//TODO FINISH WRAPPER
class WrappedPacketOutScoreboardTeam extends WrappedPacket {
    private static Class<? extends Enum<?>> enumChatFormat;
    public WrappedPacketOutScoreboardTeam(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        enumChatFormat = NMSUtils.getNMSEnumClassWithoutException("EnumChatFormat");
        if (enumChatFormat ==  null) {
            enumChatFormat= NMSUtils.getNMEnumClassWithoutException("EnumChatFormat");
        }
        net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardTeam a0;
        net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam a1;
        net.minecraft.server.v1_9_R1.PacketPlayOutScoreboardTeam a2;
        net.minecraft.server.v1_9_R2.PacketPlayOutScoreboardTeam a3;
        net.minecraft.server.v1_12_R1.PacketPlayOutScoreboardTeam a4;
        net.minecraft.server.v1_13_R1.PacketPlayOutScoreboardTeam a5;
        net.minecraft.server.v1_13_R2.PacketPlayOutScoreboardTeam a6;
        net.minecraft.server.v1_16_R2.PacketPlayOutScoreboardTeam a7;
        //Add 1.17 support, its very diff
        //net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam a8;

    }

    public String getTeamName() {
        return readString(0);
    }

    public void setTeamName(String name) {
        writeString(0, name);
    }

    public String getTeamPrefix() {
        if (version.isNewerThanOrEquals(ServerVersion.v_1_13)) {
              return readIChatBaseComponent(0);
        }
        else {
            return readString(1);
        }
    }

    public void setTeamPrefix(String teamPrefix) {
        if (version.isNewerThanOrEquals(ServerVersion.v_1_13)) {
            writeIChatBaseComponent(0, teamPrefix);
        }
        else {
            writeString(1, teamPrefix);
        }
    }

    public String getTeamSuffix() {
        if (version.isNewerThanOrEquals(ServerVersion.v_1_13)) {
            return readIChatBaseComponent(1);
        }
        else {
            return readString(2);
        }
    }

    public void setTeamSuffix(String teamSuffix) {
        if (version.isNewerThanOrEquals(ServerVersion.v_1_13)) {
            writeIChatBaseComponent(1, teamSuffix);
        }
        else {
            writeString(2, teamSuffix);
        }
    }

    public ChatColor getTeamColor() {
        Enum<?> enumChatFormatConstant = readEnumConstant(0, enumChatFormat);
        return ChatColor.values()[enumChatFormatConstant.ordinal()];
    }

    public void setTeamColor(ChatColor color) {
        Enum<?> enumChatFormatConstant = EnumUtil.valueByIndex(enumChatFormat, color.ordinal());
        writeEnumConstant(0, enumChatFormatConstant);
    }
}
