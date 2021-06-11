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

package io.github.retrooper.packetevents.packetwrappers.play.in.armanimation;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.Hand;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

public class WrappedPacketInArmAnimation extends WrappedPacket {
    private static boolean v_1_9;
    public WrappedPacketInArmAnimation(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        v_1_9 = version.isNewerThanOrEquals(ServerVersion.v_1_9);
    }

    public Hand getHand() {
        if (v_1_9) {
            Enum<?> enumConst = readEnumConstant(0, NMSUtils.enumHandClass);
            return Hand.valueOf(enumConst.name());
        }
        else {
            return Hand.MAIN_HAND;
        }
    }

    public void setHand(Hand hand) {
        //Optimize to do nothing on legacy versions. The protocol of the legacy versions only support one hand, the main hand.
        if (v_1_9) {
            Enum<?> enumConst = EnumUtil.valueOf(NMSUtils.enumHandClass, hand.name());
            writeEnumConstant(0, enumConst);
        }
    }
}
