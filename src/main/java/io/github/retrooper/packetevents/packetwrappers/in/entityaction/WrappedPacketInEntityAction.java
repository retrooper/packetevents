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

package io.github.retrooper.packetevents.packetwrappers.in.entityaction;

import io.github.retrooper.packetevents.annotations.Nullable;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import org.bukkit.entity.Entity;

import java.util.HashMap;

public final class WrappedPacketInEntityAction extends WrappedPacket {
    private static final HashMap<String, PlayerAction> cachedPlayerActionNames = new HashMap<>();
    private static final HashMap<Integer, PlayerAction> cachedPlayerActionIDs = new HashMap<>();
    @Nullable
    private static Class<?> enumPlayerActionClass;
    private static boolean isLowerThan_v_1_8;
    private Entity entity;
    private int entityId = -1;

    public WrappedPacketInEntityAction(final Object packet) {
        super(packet);
    }

    public static void load() {
        Class<?> entityActionClass = NMSUtils.getNMSClassWithoutException("PacketPlayInEntityAction");
        isLowerThan_v_1_8 = version.isLowerThan(ServerVersion.v_1_8);
        if (!isLowerThan_v_1_8) {
            enumPlayerActionClass = SubclassUtil.getSubClass(entityActionClass, "EnumPlayerAction");
        }
        //All the already existing values
        for (PlayerAction val : PlayerAction.values()) {
            cachedPlayerActionNames.put(val.name(), val);
        }

        cachedPlayerActionNames.put("PRESS_SHIFT_KEY", PlayerAction.START_SNEAKING);
        cachedPlayerActionNames.put("RELEASE_SHIFT_KEY", PlayerAction.STOP_SNEAKING);
        cachedPlayerActionNames.put("RIDING_JUMP", PlayerAction.START_RIDING_JUMP);

        cachedPlayerActionIDs.put(1, PlayerAction.START_SNEAKING);
        cachedPlayerActionIDs.put(2, PlayerAction.STOP_SNEAKING);
        cachedPlayerActionIDs.put(3, PlayerAction.STOP_SLEEPING);
        cachedPlayerActionIDs.put(4, PlayerAction.START_SPRINTING);
        cachedPlayerActionIDs.put(5, PlayerAction.STOP_SPRINTING);
        cachedPlayerActionIDs.put(6, PlayerAction.START_RIDING_JUMP); //riding jump
        cachedPlayerActionIDs.put(7, PlayerAction.OPEN_INVENTORY); //horse interaction

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
            int animationIndex = readInt(1);
            return cachedPlayerActionIDs.get(animationIndex);
        } else {
            final Object enumObj = readObject(0, enumPlayerActionClass);
            final String enumValueName = enumObj.toString();
            return cachedPlayerActionNames.get(enumValueName);
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
        START_SNEAKING,
        STOP_SNEAKING,
        STOP_SLEEPING,
        START_SPRINTING,
        STOP_SPRINTING,
        START_RIDING_JUMP,
        STOP_RIDING_JUMP,
        OPEN_INVENTORY,
        START_FALL_FLYING
    }

}
