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

package io.github.retrooper.packetevents.utils.dependencies.viaversion;

import com.github.retrooper.packetevents.protocol.ConnectionState;
import org.bukkit.entity.Player;

public interface ViaVersionAccessor {
    int getProtocolVersion(Player player);

    boolean isDebug();

    Exception throwCancelDecoderException(Throwable throwable);

    Exception throwCancelEncoderException(Throwable throwable);

    void transformPacket(Object userConnectionObj, Object byteBufObj, boolean clientSide) throws Exception;

    void setUserConnectionActive(Object userConnectionObj, boolean active);

    boolean isUserConnectionActive(Object userConnectionObj);

    boolean checkServerboundPacketUserConnection(Object userConnectionObj);

    boolean checkClientboundPacketUserConnection(Object userConnectionObj);

    ConnectionState getUserConnectionProtocolState(Object userConnectionObj);

    Class<?> getUserConnectionClass();

    Class<?> getBukkitDecodeHandlerClass();

    Class<?> getBukkitEncodeHandlerClass();

    Class<?> getCancelCodecExceptionClass();

    Class<?> getInformativeExceptionClass();
}
