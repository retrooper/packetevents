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

package io.github.retrooper.packetevents.packetwrappers.play.in.blockplace;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.Direction;
import io.github.retrooper.packetevents.utils.player.Hand;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.vector.Vector3f;
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;


public final class WrappedPacketInBlockPlace extends WrappedPacket {
    private static boolean newerThan_v_1_8_8, newerThan_v_1_7_10;
    private static int handEnumIndex;

    public WrappedPacketInBlockPlace(final NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        newerThan_v_1_7_10 = version.isNewerThan(ServerVersion.v_1_7_10);
        newerThan_v_1_8_8 = version.isNewerThan(ServerVersion.v_1_8_8);
        try {
            Object handEnum = readObject(1, NMSUtils.enumHandClass);
            handEnumIndex = 1;
        } catch (Exception ex) {
            handEnumIndex = 0;//Most likely a newer version
        }
    }

    public Hand getHand() {
        if (version.isOlderThan(ServerVersion.v_1_9)) {
            return Hand.MAIN_HAND;
        } else {
            Enum<?> enumConst = readEnumConstant(handEnumIndex, NMSUtils.enumHandClass);
            return Hand.valueOf(enumConst.name());
        }
    }

    public void setHand(Hand hand) {
        //Optimize to do nothing on legacy versions. The protocol of the legacy versions only support one hand, the main hand.
        if (version.isNewerThan(ServerVersion.v_1_8_8)) {
            Enum<?> enumConst = EnumUtil.valueOf(NMSUtils.enumHandClass, hand.name());
            write(NMSUtils.enumHandClass, handEnumIndex, enumConst);
        }
    }

    public Direction getDirection() {
        if (newerThan_v_1_8_8) {
            WrappedPacketInBlockPlace_1_9 blockPlace_1_9 = new WrappedPacketInBlockPlace_1_9(new NMSPacket(packet.getRawNMSPacket()));
            return blockPlace_1_9.getDirection();
        } else if (newerThan_v_1_7_10) {
            WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(new NMSPacket(packet.getRawNMSPacket()));
            return Direction.getDirection(blockPlace_1_8.getFace());
        } else {
            WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(new NMSPacket(packet.getRawNMSPacket()));
            return Direction.getDirection(blockPlace_1_7_10.getFace());
        }
    }

    public void setDirection(Direction direction) {
        if (newerThan_v_1_8_8) {
            WrappedPacketInBlockPlace_1_9 blockPlace_1_9 = new WrappedPacketInBlockPlace_1_9(new NMSPacket(packet.getRawNMSPacket()));
            blockPlace_1_9.setDirection(direction);
        } else if (newerThan_v_1_7_10) {
            WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(new NMSPacket(packet.getRawNMSPacket()));
            blockPlace_1_8.setFace(direction.getFaceValue());
        } else {
            WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(new NMSPacket(packet.getRawNMSPacket()));
            blockPlace_1_7_10.setFace(direction.getFaceValue());
        }
    }

    public Vector3i getBlockPosition() {
        Vector3i blockPos;
        if (newerThan_v_1_8_8) {
            WrappedPacketInBlockPlace_1_9 blockPlace_1_9 = new WrappedPacketInBlockPlace_1_9(packet);
            blockPos = blockPlace_1_9.getBlockPosition();
        } else if (newerThan_v_1_7_10) {
            WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(packet);
            blockPos = blockPlace_1_8.getBlockPosition();
        } else {
            WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(packet);
            blockPos = blockPlace_1_7_10.getBlockPosition();
        }
        return blockPos;
    }

    public Optional<Vector3f> getCursorPosition() {
        if (newerThan_v_1_8_8) {
            return Optional.empty();
        } else if (newerThan_v_1_7_10) {
            WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(packet);
            return Optional.of(blockPlace_1_8.getCursorPosition());
        } else {
            WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(packet);
            return Optional.of(blockPlace_1_7_10.getCursorPosition());
        }
    }

    public void setCursorPosition(Vector3f cursorPos) {
        if (newerThan_v_1_8_8) {

        } else if (newerThan_v_1_7_10) {
            WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(packet);
            blockPlace_1_8.setCursorPosition(cursorPos);
        } else {
            WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(packet);
            blockPlace_1_7_10.setCursorPosition(cursorPos);
        }
    }

    public Optional<ItemStack> getItemStack() {
        if (newerThan_v_1_8_8) {
            return Optional.empty();
        } else if (newerThan_v_1_7_10) {
            WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(packet);
            return Optional.of(blockPlace_1_8.getItemStack());
        } else {
            WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(packet);
            return Optional.of(blockPlace_1_7_10.getItemStack());
        }
    }


    public void setItemStack(ItemStack stack) {
        if (newerThan_v_1_8_8) {

        } else if (newerThan_v_1_7_10) {
            WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(packet);
            blockPlace_1_8.setItemStack(stack);
        } else {
            WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(packet);
            blockPlace_1_7_10.setItemStack(stack);
        }
    }
}
