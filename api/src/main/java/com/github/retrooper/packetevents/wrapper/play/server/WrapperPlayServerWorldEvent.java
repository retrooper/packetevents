package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

class WrapperPlayServerWorldEvent extends PacketWrapper<WrapperPlayServerWorldEvent> {
        private int event;
        private Vector3i location;
        private int data;
        private boolean globalEvent;


        public WrapperPlayServerWorldEvent(PacketReceiveEvent event) {
            super(event);
        }

        public WrapperPlayServerWorldEvent(int type, Vector3i location, int data, boolean globalEvent) {
            super(PacketType.Play.Server.WORLD_EVENT);
            this.event = type;
            this.location = location;
            this.data = data;
            this.globalEvent = globalEvent;
        }

    @Override
    public void read() {
        this.event = readInt();
        this.location = readBlockPosition();
        this.data = readInt();
        this.globalEvent = readBoolean();
    }

    @Override
    public void write() {
        writeInt(event);
        writeBlockPosition(location);
        writeInt(data);
        writeBoolean(globalEvent);
    }

    @Override
    public void copy(WrapperPlayServerWorldEvent wrapper) {
        this.event = wrapper.event;
        this.location = wrapper.location;
        this.data = wrapper.data;
        this.globalEvent = wrapper.globalEvent;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public Vector3i getLocation() {
        return location;
    }

    public void setLocation(Vector3i location) {
        this.location = location;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public boolean isGlobalEvent() {
        return globalEvent;
    }

    public void setGlobalEvent(boolean globalEvent) {
        this.globalEvent = globalEvent;
    }
}