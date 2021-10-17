package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerDisplayScoreboard extends PacketWrapper<WrapperPlayServerDisplayScoreboard> {
    private int position;
    private String scoreName;

    public WrapperPlayServerDisplayScoreboard(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerDisplayScoreboard(int position, String scoreName) {
        super(PacketType.Play.Server.DISPLAY_SCOREBOARD);
        this.position = position;
        this.scoreName = scoreName;
    }

    @Override
    public void readData() {
        position= readByte();
        scoreName = readString(16);
    }

    @Override
    public void readData(WrapperPlayServerDisplayScoreboard wrapper) {
        position = wrapper.position;
        scoreName = wrapper.scoreName;
    }

    @Override
    public void writeData() {
        writeByte(position);
        writeString(scoreName, 16);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getScoreName() {
        return scoreName;
    }

    public void setScoreName(String scoreName) {
        this.scoreName = scoreName;
    }
}
