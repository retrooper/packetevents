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

package io.github.retrooper.packetevents.packetwrappers.login.in.start;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.gameprofile.GameProfileUtil;
import io.github.retrooper.packetevents.utils.gameprofile.WrappedGameProfile;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;

public class WrappedPacketLoginInStart extends WrappedPacket {
    public WrappedPacketLoginInStart(NMSPacket packet) {
        super(packet);
    }

    public WrappedGameProfile getGameProfile() {
        return GameProfileUtil.getWrappedGameProfile(readObject(0, NMSUtils.gameProfileClass));
    }

    public void setGameProfile(WrappedGameProfile wrappedGameProfile) {
        Object gameProfile = GameProfileUtil.getGameProfile(wrappedGameProfile.getId(), wrappedGameProfile.getName());
        write(NMSUtils.gameProfileClass, 0, gameProfile);
    }

    @Override
    public boolean isSupported() {
        return PacketTypeClasses.Login.Client.START != null;
    }
}
