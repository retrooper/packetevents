package com.github.retrooper.packetevents.event.simple;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.exception.PacketProcessException;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.User;

public class PacketHandshakeSendEvent extends PacketSendEvent {
    public PacketHandshakeSendEvent(Object channel, User user,
                                       Object player, Object rawByteBuf,
                                       boolean autoProtocolTranslation) throws PacketProcessException {
        super(channel, user, player, rawByteBuf, autoProtocolTranslation);
    }

    protected PacketHandshakeSendEvent(int packetId, PacketTypeCommon packetType,
                                          ServerVersion serverVersion,
                                          Object channel,
                                          User user, Object player, Object byteBuf) throws PacketProcessException {
        super(packetId, packetType, serverVersion, channel, user, player, byteBuf);
    }

    @Override
    public PacketHandshakeSendEvent clone() {
        try {
            Object clonedBuffer = ByteBufHelper.retainedDuplicate(getByteBuf());
            return new PacketHandshakeSendEvent(getPacketId(), getPacketType(), getServerVersion(),
                    getChannel(), getUser(), getPlayer(), clonedBuffer);
        } catch (PacketProcessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PacketType.Handshaking.Client getPacketType() {
        return (PacketType.Handshaking.Client) super.getPacketType();
    }
}
