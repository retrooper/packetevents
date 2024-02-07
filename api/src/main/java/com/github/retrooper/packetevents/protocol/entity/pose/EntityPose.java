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

package com.github.retrooper.packetevents.protocol.entity.pose;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;

public enum EntityPose {
    STANDING,
    FALL_FLYING,
    SLEEPING,
    SWIMMING,
    SPIN_ATTACK,
    CROUCHING,
    LONG_JUMPING,
    DYING,
    CROAKING,
    USING_TONGUE,
    SITTING,
    ROARING,
    SNIFFING,
    EMERGING,
    DIGGING,
    //Added in 1.20.4
    SLIDING,
    SHOOTING,
    INHALING;

   public int getId(ClientVersion version) {
       if (this == DYING && version.isOlderThan(ClientVersion.V_1_17)) {
           return 6;
       }
       if (this.ordinal() >= 11 && version.isOlderThan(ClientVersion.V_1_19_3)) {
           return this.ordinal() - 1;
       }
       return ordinal();
   }

   public static EntityPose getById(ClientVersion version, int id) {
       // The LONG_JUMPING pose was added in 1.17, shifting things by 1
       if (id == 6 && version.isOlderThan(ClientVersion.V_1_17)) {
           return DYING;
       }
       // The SITTING pose was added in 1.19.3, shifting Warden's poses by 1
       if (id >= 10 && version.isOlderThan(ClientVersion.V_1_19_3)) {
           id++;
       }
       return values()[id];
   }
}
