package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.util.AdventureSerializer;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;

// TODO: Test on outdated versions
public class WrapperPlayServerOpenWindow extends PacketWrapper<WrapperPlayServerOpenWindow> {
    public static boolean HANDLE_JSON = true;

    private int containerId; // All versions

    private int type; // 1.14+... also 1.7. Not 1.8-1.13 though.

    private String legacyType; // 1.13-
    private int legacySlots; // 1.13-
    private int horseId; // 1.13-

    private String titleRawJson;
    private Component titleComponent; // 1.8 and above

    private boolean useProvidedWindowTitle; // 1.7 only

    public WrapperPlayServerOpenWindow(PacketSendEvent event) {
        super(event);
    }

    // For 1.14+
    public WrapperPlayServerOpenWindow(int containerId, int type, String titleRawJson) {
        super(PacketType.Play.Server.OPEN_WINDOW);
        this.containerId = containerId;
        this.type = type;
        this.titleRawJson = titleRawJson;
    }

    // 1.8 through 1.13
    public WrapperPlayServerOpenWindow(int containerId, String legacyType, int legacySlots, int horseId) {
        super(PacketType.Play.Server.OPEN_WINDOW);
        this.containerId = containerId;
        this.legacyType = legacyType;
        this.legacySlots = legacySlots;
        this.horseId = horseId;
    }

    // 1.7
    public WrapperPlayServerOpenWindow(int containerId, int type, String title, int legacySlots, boolean useProvidedWindowTitle, int horseId) {
        super(PacketType.Play.Server.OPEN_WINDOW);
        this.containerId = containerId;
        this.type = type;
        this.titleRawJson = title;
        this.legacySlots = legacySlots;
        this.useProvidedWindowTitle = useProvidedWindowTitle;
        this.horseId = horseId;
    }

    @Override
    public void readData() {
        this.containerId = readVarInt(); // All versions

        // 1.7 has a very different packet format
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
            this.type = readUnsignedByte();
            this.titleRawJson = readString();
            this.legacySlots = readUnsignedByte();
            this.useProvidedWindowTitle = readBoolean();

            if (this.type == 11) { // AnimalChest type ID
                this.horseId = readInt();
            }
            return;
        }

        // Known to be 1.8 or above
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
            this.type = readVarInt();
        } else {
            this.legacyType = readString(32);
            this.legacySlots = readUnsignedByte();

            // This is only sent for horses
            if (legacyType.equals("EntityHorse")) {
                this.horseId = readInt();
            }
        }

        this.titleRawJson = readString(getMaxMessageLength());

        if (HANDLE_JSON) {
            titleComponent = AdventureSerializer.parseComponent(this.titleRawJson);
        }
    }

    @Override
    public void readData(WrapperPlayServerOpenWindow wrapper) {
        this.containerId = wrapper.containerId;
        this.type = wrapper.type;
        this.legacyType = wrapper.legacyType;
        this.legacySlots = wrapper.legacySlots;
        this.horseId = wrapper.horseId;
        this.titleRawJson = wrapper.titleRawJson;
        this.titleComponent = wrapper.titleComponent;
        this.useProvidedWindowTitle = wrapper.useProvidedWindowTitle;
    }

    @Override
    public void writeData() {
        writeVarInt(this.containerId);

        // 1.7 has a very different packet format
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
            writeByte(this.type);
            writeString(this.titleRawJson);
            writeByte(this.legacySlots);
            writeBoolean(this.useProvidedWindowTitle);

            if (this.type == 11) { // AnimalChest type ID
                writeInt(this.horseId);
            }
            return;
        }

        // Known to be 1.8 or above
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
            writeVarInt(this.type);
        } else {
            writeString(this.legacyType);
            writeByte(this.legacySlots);

            // This is only sent for horses
            if (legacyType.equals("EntityHorse")) {
                writeInt(this.horseId);
            }
        }

        if (HANDLE_JSON) {
            titleRawJson = AdventureSerializer.toJson(titleComponent);
        }
        writeString(titleRawJson, getMaxMessageLength());
    }

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLegacyType() {
        return legacyType;
    }

    public void setLegacyType(String legacyType) {
        this.legacyType = legacyType;
    }

    public int getLegacySlots() {
        return legacySlots;
    }

    public void setLegacySlots(int legacySlots) {
        this.legacySlots = legacySlots;
    }

    public int getHorseId() {
        return horseId;
    }

    public void setHorseId(int horseId) {
        this.horseId = horseId;
    }

    public String getTitleRawJson() {
        return titleRawJson;
    }

    public void setTitleRawJson(String titleRawJson) {
        this.titleRawJson = titleRawJson;
    }

    public Component getTitleComponent() {
        return titleComponent;
    }

    public void setTitleComponent(Component titleComponent) {
        this.titleComponent = titleComponent;
    }

    public boolean isUseProvidedWindowTitle() {
        return useProvidedWindowTitle;
    }

    public void setUseProvidedWindowTitle(boolean useProvidedWindowTitle) {
        this.useProvidedWindowTitle = useProvidedWindowTitle;
    }
}
