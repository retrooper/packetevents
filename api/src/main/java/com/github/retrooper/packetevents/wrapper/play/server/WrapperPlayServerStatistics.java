package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Map;

public class WrapperPlayServerStatistics extends PacketWrapper<WrapperPlayServerStatistics> {
    // All statistic names, reference links: https://gist.github.com/thinkofname/a1842c21a0cf2e1fb5e0
    // key is the statistic name, value is the statistic value or achievement progress
    private Map<String, Integer> statistics;

    public WrapperPlayServerStatistics(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerStatistics(Map<String, Integer> statistics) {
        super(PacketType.Play.Server.STATISTICS);
        this.statistics = statistics;
    }

    @Override
    public void read() {
        statistics = readMap(PacketWrapper::readString, PacketWrapper::readVarInt);
    }

    @Override
    public void write() {
        writeMap(statistics, PacketWrapper::writeString, PacketWrapper::writeVarInt);
    }

    @Override
    public void copy(WrapperPlayServerStatistics wrapper) {
        this.statistics = wrapper.statistics;
    }

    public Map<String, Integer> getStatistics() {
        return statistics;
    }

    public void setStatistics(Map<String, Integer> statistics) {
        this.statistics = statistics;
    }
}
