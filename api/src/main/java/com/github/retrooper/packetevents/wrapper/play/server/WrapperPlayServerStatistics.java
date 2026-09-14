package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class WrapperPlayServerStatistics extends PacketWrapper<WrapperPlayServerStatistics> {
    // All statistic names, reference links: https://gist.github.com/thinkofname/a1842c21a0cf2e1fb5e0
    // key is the statistic name, value is the statistic value or achievement progress
    private Map<String, Integer> statistics;
    private List<StatisticEntry> statisticEntries;

    public WrapperPlayServerStatistics(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerStatistics(Map<String, Integer> statistics) {
        super(PacketType.Play.Server.STATISTICS);
        this.statistics = statistics;
    }

    public WrapperPlayServerStatistics(List<StatisticEntry> statisticEntries) {
        super(PacketType.Play.Server.STATISTICS);
        this.statisticEntries = statisticEntries;
    }

    @Override
    public void read() {
        /* 1.13 dropped the string keyed statistics in favor of registry ids,
           reading the legacy map on newer versions corrupts the reader index */
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            statisticEntries = readList(wrapper -> new StatisticEntry(
                    wrapper.readVarInt(), wrapper.readVarInt(), wrapper.readVarInt()));
        } else {
            statistics = readMap(PacketWrapper::readString, PacketWrapper::readVarInt);
        }
    }

    @Override
    public void write() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            writeList(statisticEntries, (wrapper, entry) -> {
                wrapper.writeVarInt(entry.getCategoryId());
                wrapper.writeVarInt(entry.getStatisticId());
                wrapper.writeVarInt(entry.getValue());
            });
        } else {
            writeMap(statistics, PacketWrapper::writeString, PacketWrapper::writeVarInt);
        }
    }

    @Override
    public void copy(WrapperPlayServerStatistics wrapper) {
        this.statistics = wrapper.statistics;
        this.statisticEntries = wrapper.statisticEntries;
    }

    /* only present on servers older than 1.13, newer versions use getStatisticEntries */
    public @Nullable Map<String, Integer> getStatistics() {
        return statistics;
    }

    public void setStatistics(Map<String, Integer> statistics) {
        this.statistics = statistics;
    }

    public @Nullable List<StatisticEntry> getStatisticEntries() {
        return statisticEntries;
    }

    public void setStatisticEntries(List<StatisticEntry> statisticEntries) {
        this.statisticEntries = statisticEntries;
    }

    public static class StatisticEntry {
        private int categoryId;
        private int statisticId;
        private int value;

        public StatisticEntry(int categoryId, int statisticId, int value) {
            this.categoryId = categoryId;
            this.statisticId = statisticId;
            this.value = value;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public int getStatisticId() {
            return statisticId;
        }

        public void setStatisticId(int statisticId) {
            this.statisticId = statisticId;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
