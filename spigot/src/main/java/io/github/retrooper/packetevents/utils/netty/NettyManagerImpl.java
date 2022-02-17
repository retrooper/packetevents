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

package io.github.retrooper.packetevents.utils.netty;

import com.github.retrooper.packetevents.netty.NettyManager;
import com.github.retrooper.packetevents.netty.buffer.ByteBufAllocationOperator;
import com.github.retrooper.packetevents.netty.buffer.ByteBufOperator;
import com.github.retrooper.packetevents.netty.channel.ChannelOperator;
import io.github.retrooper.packetevents.utils.SpigotReflectionUtil;
import io.github.retrooper.packetevents.utils.netty.buffer.*;
import io.github.retrooper.packetevents.utils.netty.channel.*;

public class NettyManagerImpl implements NettyManager {
    private static ByteBufOperator BYTE_BUF_OPERATOR;
    private static ByteBufAllocationOperator BYTE_BUF_ALLOCATION_OPERATOR;
    private static ChannelOperator CHANNEL_OPERATOR;

    @Override
    public ChannelOperator getChannelOperator() {
        if (CHANNEL_OPERATOR == null) {
            if (SpigotReflectionUtil.USE_MODERN_NETTY_PACKAGE) {
                CHANNEL_OPERATOR = new ChannelOperatorModernImpl();
            }
            else {
                CHANNEL_OPERATOR = new ChannelOperatorLegacyImpl();
            }
        }
        return CHANNEL_OPERATOR;
    }

    @Override
    public ByteBufOperator getByteBufOperator() {
        if (BYTE_BUF_OPERATOR == null) {
            if (SpigotReflectionUtil.USE_MODERN_NETTY_PACKAGE) {
                BYTE_BUF_OPERATOR = new ByteBufOperatorModernImpl();
            } else {
                BYTE_BUF_OPERATOR = new ByteBufOperatorLegacyImpl();
            }
        }
        return BYTE_BUF_OPERATOR;
    }

    @Override
    public ByteBufAllocationOperator getByteBufAllocationOperator() {
        if (BYTE_BUF_ALLOCATION_OPERATOR == null) {
            if (SpigotReflectionUtil.USE_MODERN_NETTY_PACKAGE) {
                BYTE_BUF_ALLOCATION_OPERATOR = new ByteBufAllocationOperatorModernImpl();
            } else {
                BYTE_BUF_ALLOCATION_OPERATOR = new ByteBufAllocationOperatorLegacyImpl();
            }
        }
        return BYTE_BUF_ALLOCATION_OPERATOR;
    }
}
