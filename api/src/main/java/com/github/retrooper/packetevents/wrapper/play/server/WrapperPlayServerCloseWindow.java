package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerCloseWindow extends PacketWrapper<WrapperPlayServerCloseWindow> {
    private int windowId;

    public WrapperPlayServerCloseWindow(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerCloseWindow(int id) {
        super(PacketType.Play.Server.CLOSE_WINDOW);
        this.windowId = id;
    }

    public WrapperPlayServerCloseWindow() {
        this(0);
    }

    @Override
    public void readData() {
        this.windowId = readUnsignedByte();
    }

    @Override
    public void readData(WrapperPlayServerCloseWindow wrapper) {
        this.windowId = wrapper.windowId;
    }

    @Override
    public void writeData() {
        writeByte(windowId);
    }

    /**
     * @deprecated Window ID is ignored by the client on all versions.
     */
    @Deprecated
    public int getWindowId() {
        return windowId;
    }

    /**
     * @deprecated Window ID is ignored by the client on all versions.
     */
    @Deprecated
    public void setWindowId(int windowId) {
        this.windowId = windowId;
    }
}
