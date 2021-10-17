package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Optional;

public class WrapperPlayServerUpdateScore extends PacketWrapper<WrapperPlayServerUpdateScore> {
    private String entityName;
    private Action action;
    private String objectiveName;
    private Optional<Integer> value;

    public enum Action {
        CREATE_OR_UPDATE_ITEM,
        REMOVE_ITEM;

        public static final Action[] VALUES = values();
    }

    public WrapperPlayServerUpdateScore(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerUpdateScore(String entityName, Action action, String objectiveName, Optional<Integer> value) {
        super(PacketType.Play.Server.UPDATE_SCORE);
        this.entityName = entityName;
        this.action = action;
        this.objectiveName = objectiveName;
        this.value = value;
    }

    @Override
    public void readData() {
        if (serverVersion == ServerVersion.v_1_7_10) {
            entityName = readString(16);
            action = Action.VALUES[readByte()];
            if (action != Action.REMOVE_ITEM) {
                objectiveName = readString(16);
                value = Optional.of(readInt());
            }
            else {
                objectiveName = "";
                value = Optional.empty();
            }
        }
        else {
            entityName = readString(40);
            action = Action.VALUES[readByte()];
            objectiveName = readString(16);
            if (action != Action.REMOVE_ITEM) {
                value = Optional.of(readVarInt());
            }
        }
    }

    @Override
    public void readData(WrapperPlayServerUpdateScore wrapper) {
        entityName = wrapper.entityName;
        action = wrapper.action;
        objectiveName = wrapper.objectiveName;
        value = wrapper.value;
    }

    @Override
    public void writeData() {
        if (serverVersion == ServerVersion.v_1_7_10) {
            writeString(entityName, 16);
            writeByte(action.ordinal());
            if (action != Action.REMOVE_ITEM) {
                writeString(objectiveName, 16);
                writeInt(value.orElse(-1));
            }
            else {
                objectiveName = "";
                value = Optional.empty();
            }
        }
        else {
            writeString(entityName, 40);
            writeByte(action.ordinal());
            writeString(objectiveName, 16);
            if (action != Action.REMOVE_ITEM) {
                writeVarInt(value.orElse(-1));
            }
        }

    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getObjectiveName() {
        return objectiveName;
    }

    public void setObjectiveName(String objectiveName) {
        this.objectiveName = objectiveName;
    }

    public Optional<Integer> getValue() {
        return value;
    }

    public void setValue(Optional<Integer> value) {
        this.value = value;
    }
}
