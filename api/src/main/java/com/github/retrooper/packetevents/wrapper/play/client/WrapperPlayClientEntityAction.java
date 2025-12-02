/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

public class WrapperPlayClientEntityAction extends PacketWrapper<WrapperPlayClientEntityAction> {
    private int entityID;
    private Action action;
    private int jumpBoost;

    public WrapperPlayClientEntityAction(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientEntityAction(int entityID, Action action, int jumpBoost) {
        super(PacketType.Play.Client.ENTITY_ACTION);
        this.entityID = entityID;
        this.action = action;
        this.jumpBoost = jumpBoost;
    }

    @Override
    public void read() {
        boolean v1_8 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8);
        entityID = v1_8 ? readVarInt() : readInt();
        int id = v1_8 ? readVarInt() : readByte();
        action = Action.getById(serverVersion, id);
        jumpBoost = v1_8 ? readVarInt() : readInt();
    }

    @Override
    public void write() {
        boolean v1_8 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8);
        if (v1_8) {
            writeVarInt(entityID);
            int actionIndex = action.getId(serverVersion);
            writeVarInt(actionIndex);
            writeVarInt(jumpBoost);
        } else {
            writeInt(entityID);
            int actionIndex = action.getId(serverVersion);
            writeByte(actionIndex);
            writeInt(jumpBoost);
        }
    }

    @Override
    public void copy(WrapperPlayClientEntityAction wrapper) {
        entityID = wrapper.entityID;
        action = wrapper.action;
        jumpBoost = wrapper.jumpBoost;
    }

    public int getEntityId() {
        return entityID;
    }

    public void setEntityId(int entityID) {
        this.entityID = entityID;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public int getJumpBoost() {
        return jumpBoost;
    }

    public void setJumpBoost(int jumpBoost) {
        this.jumpBoost = jumpBoost;
    }

    public enum Action {

        /**
         * Removed with 1.21.6
         */
        @ApiStatus.Obsolete
        START_SNEAKING {
            @Override
            public int getId(ServerVersion version) {
                return version.isNewerThanOrEquals(ServerVersion.V_1_21_6)
                        ? -1 : version.isOlderThanOrEquals(ServerVersion.V_1_7_10)
                        ? 1 : this.ordinal();
            }
        },
        /**
         * Removed with 1.21.6
         */
        @ApiStatus.Obsolete
        STOP_SNEAKING {
            @Override
            public int getId(ServerVersion version) {
                return version.isNewerThanOrEquals(ServerVersion.V_1_21_6)
                        ? -1 : version.isOlderThanOrEquals(ServerVersion.V_1_7_10)
                        ? 2 : this.ordinal();
            }
        },
        LEAVE_BED {
            @Override
            public int getId(ServerVersion version) {
                if (version.isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
                    return this.ordinal() - 2;
                }
                return version.isOlderThanOrEquals(ServerVersion.V_1_7_10) ? 3 : this.ordinal();
            }
        },
        START_SPRINTING {
            @Override
            public int getId(ServerVersion version) {
                if (version.isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
                    return this.ordinal() - 2;
                }
                return version.isOlderThanOrEquals(ServerVersion.V_1_7_10) ? 4 : this.ordinal();
            }
        },
        STOP_SPRINTING {
            @Override
            public int getId(ServerVersion version) {
                if (version.isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
                    return this.ordinal() - 2;
                }
                return version.isOlderThanOrEquals(ServerVersion.V_1_7_10) ? 5 : this.ordinal();
            }
        },
        START_JUMPING_WITH_HORSE {
            @Override
            public int getId(ServerVersion version) {
                if (version.isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
                    return this.ordinal() - 2;
                }
                return version.isOlderThanOrEquals(ServerVersion.V_1_7_10) ?
                        (version.isOlderThanOrEquals(ServerVersion.V_1_7_5) ? -1 : 6) : this.ordinal();
            }
        },
        /**
         * Added with 1.9
         */
        STOP_JUMPING_WITH_HORSE {
            @Override
            public int getId(ServerVersion version) {
                if (version.isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
                    return this.ordinal() - 2;
                } else if (version.isNewerThanOrEquals(ServerVersion.V_1_9)) {
                    return this.ordinal();
                }
                return -1;
            }
        },
        OPEN_HORSE_INVENTORY {
            @Override
            public int getId(ServerVersion version) {
                if (version.isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
                    return this.ordinal() - 2;
                } else if (version.isNewerThanOrEquals(ServerVersion.V_1_9)) {
                    return this.ordinal();
                }
                return this.ordinal() - 1;
            }
        },
        /**
         * Added with 1.9
         */
        START_FLYING_WITH_ELYTRA {
            @Override
            public int getId(ServerVersion version) {
                if (version.isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
                    return this.ordinal() - 2;
                } else if (version.isNewerThanOrEquals(ServerVersion.V_1_9)) {
                    return this.ordinal();
                }
                return -1;
            }
        },
        ;

        private static final Action[] VALUES = values();

        public abstract int getId(ServerVersion version);

        public static Action getById(ServerVersion version, int id) {
            for (Action action : VALUES) {
                if (action.getId(version) == id) {
                    return action;
                }
            }
            throw new IllegalStateException("Invalid entity action id " + id + " for " + version);
        }
    }
}
