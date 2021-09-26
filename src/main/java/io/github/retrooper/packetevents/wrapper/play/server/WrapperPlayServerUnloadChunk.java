package io.github.retrooper.packetevents.wrapper.play.server;

import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.protocol.packettype.PacketType;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerUnloadChunk extends PacketWrapper<WrapperPlayServerUnloadChunk> {
    private int chunkX, chunkZ;

    public WrapperPlayServerUnloadChunk(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerUnloadChunk(int chunkX, int chunkZ) {
        super(PacketType.Play.Server.UNLOAD_CHUNK);
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    @Override
    public void readData() {
        this.chunkX = readInt();
        this.chunkZ = readInt();
    }

    @Override
    public void readData(WrapperPlayServerUnloadChunk wrapper) {
        this.chunkX = wrapper.chunkX;
        this.chunkZ = wrapper.chunkZ;
    }

    @Override
    public void writeData() {
        writeInt(chunkX);
        writeInt(chunkZ);
    }

    public int getChunkX() {
        return chunkX;
    }

    public void setChunkX(int chunkX) {
        this.chunkX = chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public void setChunkZ(int chunkZ) {
        this.chunkZ = chunkZ;
    }
}
