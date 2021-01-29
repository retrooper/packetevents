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
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

public final class WrappedPacketInEntityAction extends WrappedPacket {
    private static Class<?> enumPlayerActionClass;
    private static boolean isLowerThan_v_1_8;
    private Entity entity;
    private int entityId = -1;

    public WrappedPacketInEntityAction(final NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        isLowerThan_v_1_8 = version.isOlderThan(ServerVersion.v_1_8);
        if (!isLowerThan_v_1_8) {
            enumPlayerActionClass = SubclassUtil.getSubClass(PacketTypeClasses.Play.Client.ENTITY_ACTION, "EnumPlayerAction");
        }
    }

    /**
     * Lookup the associated entity by the ID that was sent in the packet.
     *
     * @return Entity
     */
    public Entity getEntity() {
        if (entity == null) {
            return entity = NMSUtils.getEntityById(getEntityId());
        }
        return entity;
    }

    /**
     * Get the ID of the entity.
     * If you do not want to use {@link #getEntity()},
     * you lookup the entity by yourself with this entity ID.
     *
     * @return Entity ID
     */
    public int getEntityId() {
        if (entityId == -1) {
            net.minecraft.server.v1_8_R3.PacketPlayInEntityAction ea;
            entityId = readInt(0);
        }
        return entityId;
    }

    /**
     * Get the player action.
     *
     * @return Player Action
     */
    public PlayerAction getAction() {
        if (isLowerThan_v_1_8) {
            byte animationIndex = (byte) readInt(1);
            return PlayerAction.getByActionValue(animationIndex);
        } else {
            final Object enumObj = readObject(0, enumPlayerActionClass);
            return PlayerAction.getByName(enumObj.toString());
        }
    }

    /**
     * Get the jump boost integer.
     *
     * @return Jump Boost
     */
    public int getJumpBoost() {
        if (isLowerThan_v_1_8) {
            return readInt(2);
        } else {
            return readInt(1);
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

        public byte getActionValue() {
            return actionID;
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
    }

}
