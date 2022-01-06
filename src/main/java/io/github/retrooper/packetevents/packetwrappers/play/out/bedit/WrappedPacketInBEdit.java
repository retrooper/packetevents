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

package io.github.retrooper.packetevents.packetwrappers.play.out.bedit;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.Hand;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

/**
 * @author Tecnio
 */
public class WrappedPacketInBEdit extends WrappedPacket {
    private static boolean v_1_13, v_1_17;

    public WrappedPacketInBEdit(final NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        v_1_13 = version.isNewerThanOrEquals(ServerVersion.v_1_13);
        v_1_17 = version.isNewerThanOrEquals(ServerVersion.v_1_17);
    }

    public ItemStack getItemStack() {
        return readItemStack(0);
    }

    public void setItemStack(final ItemStack itemStack) {
        writeItemStack(0, itemStack);
    }

    public boolean isSigning() {
        return readBoolean(0);
    }

    public void setSigning(final boolean signing) {
        writeBoolean(0, signing);
    }

    @SupportedVersions(ranges = {ServerVersion.v_1_13, ServerVersion.ERROR})
    public Optional<Hand> getHand() {
        if (v_1_17) {
            return Optional.of(Hand.values()[readInt(0)]);
        } else if (v_1_13) {
            final Enum<?> enumConst = readEnumConstant(0, NMSUtils.enumHandClass);
            return Optional.of(Hand.valueOf(enumConst.name()));
        }

        return Optional.empty();
    }

    @SupportedVersions(ranges = {ServerVersion.v_1_13, ServerVersion.ERROR})
    public void setHand(final Hand hand) {
        if (v_1_17) {
            writeInt(0, hand.ordinal());
        } else if (v_1_13) {
            final Enum<?> enumConst = EnumUtil.valueOf(NMSUtils.enumHandClass, hand.name());
            writeEnumConstant(0, enumConst);
        }
    }
}
