package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WrapperPlayServerScoreboardObjective extends PacketWrapper<WrapperPlayServerScoreboardObjective> {

    private String name;
    private ObjectiveMode mode;
    private Optional<String> displayName;
    private Optional<HealthDisplay> display;

    enum HealthDisplay {
        INTEGER,
        HEARTS;

        @Nullable
        public static HealthDisplay getByName(String name) {
            for (HealthDisplay display : values()) {
                if (display.name().equalsIgnoreCase(name)) {
                    return display;
                }
            }

            return null;
        }
    }

    enum ObjectiveMode {
        CREATE,
        REMOVE,
        UPDATE;
    }

    public WrapperPlayServerScoreboardObjective(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayServerScoreboardObjective(String name, ObjectiveMode mode, Optional<String> displayName, Optional<HealthDisplay> display) {
        super(PacketType.Play.Server.SCOREBOARD_OBJECTIVE);
        this.name = name;
        this.mode = mode;
        this.displayName = displayName;
        this.display = display;
    }

    @Override
    public void readData() {
        name = readString();
        mode = ObjectiveMode.values()[readByte()];
        displayName = Optional.ofNullable(readString());
        display = Optional.ofNullable(HealthDisplay.getByName(readString()));
    }

    @Override
    public void readData(WrapperPlayServerScoreboardObjective wrapper) {
        name = wrapper.name;
        mode = wrapper.mode;
        displayName = wrapper.displayName;
        display = wrapper.display;
    }

    @Override
    public void writeData() {
        writeString(name);
        writeByte((byte) mode.ordinal());
        if (mode == ObjectiveMode.CREATE || mode == ObjectiveMode.UPDATE) {
            writeString(displayName.orElse(""));
            if (serverVersion == ServerVersion.V_1_7_10)
                writeString("integer");
            else
                writeString(display.orElse(HealthDisplay.INTEGER).name().toLowerCase());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectiveMode getMode() {
        return mode;
    }

    public void setMode(ObjectiveMode mode) {
        this.mode = mode;
    }

    public Optional<String> getDisplayName() {
        return displayName;
    }

    public void setDisplayName(Optional<String> displayName) {
        this.displayName = displayName;
    }

    public Optional<HealthDisplay> getDisplay() {
        return display;
    }

    public void setDisplay(Optional<HealthDisplay> display) {
        this.display = display;
    }

}