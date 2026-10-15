package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mojang name: ClientboundAwardStatsPacket
 */
public class WrapperPlayServerStatistics extends PacketWrapper<WrapperPlayServerStatistics> {

    /**
     * Some (outdated) statistic names: <a href="https://gist.github.com/thinkofname/a1842c21a0cf2e1fb5e0">https://gist.github.com/thinkofname/a1842c21a0cf2e1fb5e0</a>;
     * key is the statistic name, value is the statistic value or achievement progress
     *
     * @versions -1.12.2
     */
    private Map<String, Integer> statistics;
    /**
     * @versions 1.13+
     */
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
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            this.statisticEntries = this.readList(StatisticEntry::read);
        } else {
            this.statistics = this.readMap(PacketWrapper::readString, PacketWrapper::readVarInt);
        }
    }

    @Override
    public void write() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            this.writeList(this.getStatisticEntries(), StatisticEntry::write);
        } else {
            this.writeMap(this.getStatistics(), PacketWrapper::writeString, PacketWrapper::writeVarInt);
        }
    }

    @Override
    public void copy(WrapperPlayServerStatistics wrapper) {
        this.statistics = wrapper.statistics;
        this.statisticEntries = wrapper.statisticEntries;
    }

    public Map<String, Integer> getStatistics() {
        if (this.statistics == null) {
            this.statistics = new HashMap<>();
        }
        return this.statistics;
    }

    public void setStatistics(Map<String, Integer> statistics) {
        this.statistics = statistics;
        this.statisticEntries = null;
    }

    public List<StatisticEntry> getStatisticEntries() {
        if (this.statisticEntries == null) {
            this.statisticEntries = new ArrayList<>();
        }
        return this.statisticEntries;
    }

    public void setStatisticEntries(List<StatisticEntry> statisticEntries) {
        this.statisticEntries = statisticEntries;
        this.statistics = null;
    }

    /**
     * This is marked as experimental, as this just provides access to raw data values - these
     * are version-specific, and we currently don't provide any API for accessing statistics without relying on IDs.
     *
     * @versions 1.13+
     */
    @ApiStatus.Experimental
    public static class StatisticEntry {

        private int categoryId;
        private int statisticId;
        private int value;

        public StatisticEntry(int categoryId, int statisticId, int value) {
            this.categoryId = categoryId;
            this.statisticId = statisticId;
            this.value = value;
        }

        public static StatisticEntry read(PacketWrapper<?> wrapper) {
            int categoryId = wrapper.readVarInt();
            int statisticId = wrapper.readVarInt();
            int value = wrapper.readVarInt();
            return new StatisticEntry(categoryId, statisticId, value);
        }

        public static void write(PacketWrapper<?> wrapper, StatisticEntry entry) {
            wrapper.writeVarInt(entry.categoryId);
            wrapper.writeVarInt(entry.statisticId);
            wrapper.writeVarInt(entry.value);
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
