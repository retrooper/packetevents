package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public class WrapperPlayServerBossBar extends PacketWrapper<WrapperPlayServerBossBar> {

    public static final byte FLAG_DARKEN_SKY = 0x1;
    public static final byte FLAG_PLAY_BOSS_MUSIC = 0x2;
    public static final byte FLAG_CREATE_FOG = 0x4; // Previously was controlled by FLAG_PLAY_BOSS_MUSIC;

    public static byte createFlags(boolean darkenSky, boolean playBossMusic, boolean createFog) {
        byte flags = 0;
        if(darkenSky) {
            flags |= FLAG_DARKEN_SKY;
        }
        if(playBossMusic) {
            flags |= FLAG_PLAY_BOSS_MUSIC;
        }
        if(createFog) {
            flags |= FLAG_CREATE_FOG;
        }
        return flags;
    }

    private UUID uuid;
    private Action action;


    public WrapperPlayServerBossBar(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerBossBar(UUID uuid, Action action) {
        super(PacketType.Play.Server.BOSS_BAR);
        this.uuid = uuid;
        this.action = action;
    }

    @Override
    public void read() {
        uuid = readUUID();
        EnumAction a = EnumAction.VALUES[readVarInt()];
        switch (a) {
            case ADD:
                action = Action.add(readComponent(), readFloat(), readColor(), Division.VALUES[readVarInt()], readByte());
                break;
            case REMOVE:
                action = Action.remove();
                break;
            case UPDATE_FLAGS:
                action = Action.updateFlags(readByte());
                break;
            case UPDATE_STYLE:
                action = Action.updateStyle(readColor(), Division.VALUES[readVarInt()]);
                break;
            case UPDATE_TITLE:
                action = Action.updateTitle(readComponent());
                break;
            case UPDATE_HEALTH:
                action = Action.updateHealth(readFloat());
                break;
        }
    }

    @Override
    public void write() {
        writeUUID(uuid);
        writeVarInt(action instanceof AddAction ? EnumAction.ADD.ordinal() : action instanceof RemoveAction ? EnumAction.REMOVE.ordinal() : action instanceof UpdateFlagsAction ? EnumAction.UPDATE_FLAGS.ordinal() : action instanceof UpdateStyleAction ? EnumAction.UPDATE_STYLE.ordinal() : action instanceof UpdateTitleAction ? EnumAction.UPDATE_TITLE.ordinal() : EnumAction.UPDATE_HEALTH.ordinal());
        if (action instanceof AddAction) {
            AddAction a = (AddAction) action;
            writeComponent(a.title);
            writeFloat(a.health);
            writeColor(a.color);
            writeVarInt(a.division.ordinal());
            writeByte(a.flags);
        } else if (action instanceof UpdateFlagsAction) {
            writeByte(((UpdateFlagsAction) action).flags);
        } else if (action instanceof UpdateStyleAction) {
            UpdateStyleAction a = (UpdateStyleAction) action;
            writeColor(a.color);
            writeVarInt(a.division.ordinal());
        } else if (action instanceof UpdateTitleAction) {
            writeComponent(((UpdateTitleAction) action).title);
        } else if (action instanceof UpdateHealthAction) {
            writeFloat(((UpdateHealthAction) action).health);
        }
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public UUID getUuid() {
        return uuid;
    }

    private Color readColor() {
        return Color.VALUES[readVarInt()];
    }

    private void writeColor(Color color) {
        writeVarInt(color.ordinal());
    }

    public enum Color {
        PINK,
        BLUE,
        RED,
        GREEN,
        YELLOW,
        PURPLE,
        WHITE;

        public static final Color[] VALUES = values();
    }

    public interface Action {

        static Action remove() {
            return new RemoveAction();
        }

        static Action updateHealth(float health) {
            return new UpdateHealthAction(health);
        }

        static Action updateTitle(Component title) {
            return new UpdateTitleAction(title);
        }

        static Action updateStyle(Color color, Division division) {
            return new UpdateStyleAction(color, division);
        }

        static Action updateFlags(byte flags) {
            return new UpdateFlagsAction(flags);
        }

        static Action add(Component title, float health, Color color, Division division, byte flags) {
            return new AddAction(title, health, color, division, flags);
        }

    }
    static final class RemoveAction implements Action {


    }
    static final class UpdateHealthAction implements Action {


        final float health;

        UpdateHealthAction(float health) {
            this.health = health;
        }

    }
    static final class UpdateTitleAction implements Action {


        final Component title;

        UpdateTitleAction(Component title) {
            this.title = title;
        }

    }
    static final class UpdateStyleAction implements Action {

        final Color color;

        final Division division;

        UpdateStyleAction(Color color, Division division) {
            this.color = color;
            this.division = division;
        }

    }
    static final class UpdateFlagsAction implements Action {


        final byte flags;

        UpdateFlagsAction(byte flags) {
            this.flags = flags;
        }

    }
    static final class AddAction implements Action {

        final Component title;
        final float health;
        final Color color;
        final Division division;

        final byte flags;

        AddAction(Component title, float health, Color color, Division division, byte flags) {
            this.title = title;
            this.health = health;
            this.color = color;
            this.division = division;
            this.flags = flags;
        }

    }

    public enum Division {
        NONE,
        NOTCHES_6,
        NOTCHES_10,
        NOTCHES_12,
        NOTCHES_20;

        public static final Division[] VALUES = values();
    }

    enum EnumAction {
        ADD,
        REMOVE,
        UPDATE_HEALTH,
        UPDATE_TITLE,
        UPDATE_STYLE,
        UPDATE_FLAGS;

        private static final EnumAction[] VALUES = values();
    }
}
