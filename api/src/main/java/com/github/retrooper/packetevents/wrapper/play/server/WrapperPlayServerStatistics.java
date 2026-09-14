package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WrapperPlayServerStatistics extends PacketWrapper<WrapperPlayServerStatistics> {
    // Legacy (<1.13): statistic name -> value. Modern (1.13+): registry category/stat ids.
    private Map<String, Integer> statistics = Collections.emptyMap();
    private List<StatisticEntry> statisticEntries = Collections.emptyList();
    private boolean modernEntries;

    public WrapperPlayServerStatistics(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerStatistics(Map<String, Integer> statistics) {
        super(PacketType.Play.Server.STATISTICS);
        setStatistics(statistics);
    }

    public WrapperPlayServerStatistics(List<StatisticEntry> statisticEntries) {
        super(PacketType.Play.Server.STATISTICS);
        setStatisticEntries(statisticEntries);
    }

    @Override
    public void read() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            this.statisticEntries = readList(wrapper -> new StatisticEntry(
                    wrapper.readVarInt(), wrapper.readVarInt(), wrapper.readVarInt()));
            this.statistics = Collections.emptyMap();
            this.modernEntries = true;
        } else {
            this.statistics = readMap(PacketWrapper::readString, PacketWrapper::readVarInt);
            this.statisticEntries = Collections.emptyList();
            this.modernEntries = false;
        }
    }

    @Override
    public void write() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            if (!modernEntries) {
                throw new IllegalStateException(
                        "Cannot write legacy statistics map on 1.13+; use setStatisticEntries(...)");
            }
            writeList(statisticEntries, (wrapper, entry) -> {
                wrapper.writeVarInt(entry.getCategoryId());
                wrapper.writeVarInt(entry.getStatisticId());
                wrapper.writeVarInt(entry.getValue());
            });
        } else {
            if (modernEntries) {
                throw new IllegalStateException(
                        "Cannot write modern statistic entries on pre-1.13; use setStatistics(...)");
            }
            writeMap(statistics, PacketWrapper::writeString, PacketWrapper::writeVarInt);
        }
    }

    @Override
    public void copy(WrapperPlayServerStatistics wrapper) {
        this.statistics = wrapper.statistics;
        this.statisticEntries = wrapper.statisticEntries;
        this.modernEntries = wrapper.modernEntries;
    }

    /**
     * Legacy string-keyed statistics. Throws if modern entries are present — use {@link #getStatisticEntries()}.
     */
    @Deprecated
    public Map<String, Integer> getStatistics() {
        if (modernEntries) {
            throw new IllegalStateException("use getStatisticEntries() on 1.13+");
        }
        return statistics;
    }

    public void setStatistics(@Nullable Map<String, Integer> statistics) {
        this.statistics = statistics != null ? statistics : Collections.emptyMap();
        this.statisticEntries = Collections.emptyList();
        this.modernEntries = false;
    }

    /**
     * Modern registry-id statistics. Throws if the legacy map is present — use {@link #getStatistics()}.
     */
    public List<StatisticEntry> getStatisticEntries() {
        if (!modernEntries) {
            throw new IllegalStateException("use getStatistics() on pre-1.13");
        }
        return statisticEntries;
    }

    public void setStatisticEntries(@Nullable List<StatisticEntry> statisticEntries) {
        this.statisticEntries = statisticEntries != null ? statisticEntries : Collections.emptyList();
        this.statistics = Collections.emptyMap();
        this.modernEntries = true;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof StatisticEntry)) return false;
            StatisticEntry that = (StatisticEntry) o;
            return categoryId == that.categoryId
                    && statisticId == that.statisticId
                    && value == that.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(categoryId, statisticId, value);
        }
    }
}
