/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.packetwrappers.play.in.entityaction;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

public final class WrappedPacketInEntityAction extends WrappedPacket {
    private static Class<? extends Enum<?>> enumPlayerActionClass;
    private static boolean olderThan_v_1_8;
    private static boolean newerThan_v_1_8_8;
    private Entity entity;
    private int entityID = -1;

    public WrappedPacketInEntityAction(final NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        olderThan_v_1_8 = version.isOlderThan(ServerVersion.v_1_8);
        newerThan_v_1_8_8 = version.isNewerThan(ServerVersion.v_1_8_8);
        if (!olderThan_v_1_8) {
            enumPlayerActionClass = SubclassUtil.getEnumSubClass(PacketTypeClasses.Play.Client.ENTITY_ACTION, "EnumPlayerAction");
        }
    }

    public Entity getEntity() {
        if (entity == null) {
            return entity = NMSUtils.getEntityById(getEntityId());
        }
        return entity;
    }

    public void setEntity(Entity entity) {
        setEntityId(entity.getEntityId());
        this.entity = entity;
    }

    public int getEntityId() {
        if (entityID == -1) {
            entityID = readInt(0);
        }
        return entityID;
    }

    public void setEntityId(int entityID) {
        writeInt(0, this.entityID = entityID);
        this.entity = null;
    }

    public PlayerAction getAction() {
        if (olderThan_v_1_8) {
            int animationIndex = readInt(1) - 1;
            return PlayerAction.getByActionValue((byte) (animationIndex));
        } else {
            Enum<?> enumConst = readEnumConstant(0, enumPlayerActionClass);
            return PlayerAction.getByName(enumConst.name());
        }
    }

    public void setAction(PlayerAction action) throws UnsupportedOperationException {
        if (olderThan_v_1_8) {
            byte animationIndex = action.actionID;
            writeInt(1, animationIndex + 1);
        } else {
            Enum<?> enumConst;
            if (newerThan_v_1_8_8) {
                if (action == PlayerAction.RIDING_JUMP) {
                    throwUnsupportedOperation(action);
                }
                enumConst = EnumUtil.valueByIndex(enumPlayerActionClass, action.ordinal());

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
        if (olderThan_v_1_8) {
            return readInt(2);
        } else {
            return readInt(1);
        }
    }

    public void setJumpBoost(int jumpBoost) {
        if (olderThan_v_1_8) {
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
