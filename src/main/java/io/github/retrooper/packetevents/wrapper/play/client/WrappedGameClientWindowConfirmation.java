package io.github.retrooper.packetevents.wrapper.game.client;

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * A response to the window confirmation packet by the client
 * It is processed on the main thread, and is therefore very useful for anticheat purposes
 *
 * If a confirmation sent by the client was not accepted, the server will reply with a
 * {@link io.github.retrooper.packetevents.wrapper.game.server.WrappedGameServerWindowConfirmation}
 * packet with the Accepted field set to false. When this happens,
 * the client must send this packet to apologize (as with movement),
 * otherwise the server ignores any successive confirmations.
 *
 * Replaced in 1.17 with the more efficient {@link WrapperGameClientPong}
 *
 * @see io.github.retrooper.packetevents.wrapper.game.server.WrappedGameServerWindowConfirmation
 */
public class WrappedGameClientWindowConfirmation extends PacketWrapper<WrappedGameClientWindowConfirmation> {
    byte windowID;
    short actionID;
    boolean accepted;

    public WrappedGameClientWindowConfirmation(PacketReceiveEvent event) {
        super(event);
    }

    public WrappedGameClientWindowConfirmation(byte windowID, short actionID, boolean accepted) {
        super(PacketType.Game.Client.WINDOW_CONFIRMATION.getID());
        this.windowID = windowID;
        this.actionID = actionID;
        this.accepted = accepted;
    }

    @Override
    public void readData() {
        this.windowID = readByte();
        this.actionID = readShort();
        this.accepted = readBoolean();
    }

    @Override
    public void readData(WrappedGameClientWindowConfirmation wrapper) {
        this.windowID = wrapper.getWindowID();
        this.actionID = wrapper.getActionID();
        this.accepted = wrapper.isAccepted();
    }

    @Override
    public void writeData() {
        writeByte(windowID);
        writeShort(actionID);
        writeBoolean(accepted);
    }

    public byte getWindowID() {
        return windowID;
    }

    public void setWindowID(byte windowID) {
        this.windowID = windowID;
    }

    public short getActionID() {
        return actionID;
    }

    public void setActionID(short actionID) {
        this.actionID = actionID;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
