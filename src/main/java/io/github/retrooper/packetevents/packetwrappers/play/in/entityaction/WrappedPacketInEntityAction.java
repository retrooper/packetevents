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

package io.github.retrooper.packetevents.packetwrappers.play.in.entityaction;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.jetbrains.annotations.Nullable;

public final class WrappedPacketInEntityAction extends WrappedPacketEntityAbstraction {
    private static Class<? extends Enum<?>> enumPlayerActionClass;
    private static boolean newerThan_v_1_8_8;

    public WrappedPacketInEntityAction(final NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        newerThan_v_1_8_8 = version.isNewerThan(ServerVersion.v_1_8_8);
        enumPlayerActionClass = NMSUtils.getNMSEnumClassWithoutException("EnumPlayerAction");
        if (enumPlayerActionClass == null) {
            enumPlayerActionClass = SubclassUtil.getEnumSubClass(PacketTypeClasses.Play.Client.ENTITY_ACTION, "EnumPlayerAction");
        }
        if (version.isNewerThanOrEquals(ServerVersion.v_1_20_6)) {
            enumPlayerActionClass = SubclassUtil.getEnumSubClass(PacketTypeClasses.Play.Client.ENTITY_ACTION, 0);
        }

    }

    public PlayerAction getAction() {
        if (enumPlayerActionClass == null) {
            int animationIndex = readInt(1) - 1;
            return PlayerAction.getByActionValue((byte) (animationIndex));
        } else {
            Enum<?> enumConst = readEnumConstant(0, enumPlayerActionClass);
            if (newerThan_v_1_8_8) {
                return PlayerAction.getByActionValue((byte) enumConst.ordinal());
            }
            return PlayerAction.getByName(enumConst.name());
        }
    }

    public void setAction(PlayerAction action) throws UnsupportedOperationException {
        if (enumPlayerActionClass == null) {
            byte animationIndex = action.actionID;
            writeInt(1, animationIndex + 1);
        } else {
            Enum<?> enumConst;
            if (newerThan_v_1_8_8) {
                if (action == PlayerAction.RIDING_JUMP) {
                    throwUnsupportedOperation(action);
                }
                enumConst = EnumUtil.valueByIndex(enumPlayerActionClass, action.getActionValue());

            } else {
                enumConst = EnumUtil.valueOf(enumPlayerActionClass, action.name());
                if (enumConst == null) {
                    enumConst = EnumUtil.valueOf(enumPlayerActionClass, action.alias);
                }
            }
            writeEnumConstant(0, enumConst);
        }
    }

    public int getJumpBoost() {
        if (enumPlayerActionClass == null) {
            return readInt(2);
        } else {
            return readInt(1);
        }
    }

    public void setJumpBoost(int jumpBoost) {
        if (enumPlayerActionClass == null) {
            writeInt(2, jumpBoost);
        } else {
            writeInt(1, jumpBoost);
        }
    }

    public enum PlayerAction {
        START_SNEAKING((byte) 0, "PRESS_SHIFT_KEY"),
        STOP_SNEAKING((byte) 1, "RELEASE_SHIFT_KEY"),
        STOP_SLEEPING((byte) 2),
        START_SPRINTING((byte) 3),
        STOP_SPRINTING((byte) 4),
        START_RIDING_JUMP((byte) 5),
        STOP_RIDING_JUMP((byte) 6),
        OPEN_INVENTORY((byte) 7),
        START_FALL_FLYING((byte) 8),

        /**
         * Removed in minecraft server version 1.9.
         * This constant will only work on 1.7.10-1.8
         */
        @SupportedVersions(ranges = {ServerVersion.v_1_7_10, ServerVersion.v_1_8_8})
        @Deprecated
        RIDING_JUMP((byte) 5);

        final byte actionID;
        final String alias;

        PlayerAction(byte actionID) {
            this.actionID = actionID;
            this.alias = "empty";
        }

        PlayerAction(byte actionID, String alias) {
            this.actionID = actionID;
            this.alias = alias;
        }

        @Nullable
        public static PlayerAction getByActionValue(byte value) {
            if (version.isOlderThan(ServerVersion.v_1_9)) {
                if (value == RIDING_JUMP.actionID) {
                    return RIDING_JUMP;
                } else {
                    for (PlayerAction action : values()) {
                        if (action.actionID == value) {
                            return action;
                        }
                    }
                }
            } else {
                return values()[value];
            }
            return null;
        }

        @Nullable
        public static PlayerAction getByName(String name) {
            for (PlayerAction action : values()) {
                if (action.name().equals(name) || action.alias.equals(name)) {
                    return action;
                }
            }
            return null;
        }

        public byte getActionValue() {
            return actionID;
        }
    }

}
