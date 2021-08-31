/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.retrooper.packetevents.packetwrappers.play.out.title;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.lang.reflect.Constructor;

public class WrappedPacketOutTitle extends WrappedPacket implements SendableWrapper {
    private static Class<? extends Enum<?>> enumTitleActionClass;
    private static Constructor<?> packetConstructor;

    private TitleAction action;
    private String text;
    private int fadeInTicks;
    private int stayTicks;
    private int fadeOutTicks;

    public WrappedPacketOutTitle(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutTitle(TitleAction action, String text, int fadeInTicks, int stayTicks, int fadeOutTicks) {
        this.action = action;
        this.text = text;
        this.fadeInTicks = fadeInTicks;
        this.stayTicks = stayTicks;
        this.fadeOutTicks = fadeOutTicks;
    }

    @Override
    protected void load() {
        enumTitleActionClass = SubclassUtil.getEnumSubClass(PacketTypeClasses.Play.Server.TITLE, 0);
        try {
            packetConstructor = PacketTypeClasses.Play.Server.TITLE.getConstructor(enumTitleActionClass, NMSUtils.iChatBaseComponentClass,
                    int.class, int.class, int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public TitleAction getAction() {
        if (packet != null) {
            Enum<?> enumConst = readEnumConstant(0, enumTitleActionClass);
            return TitleAction.values()[enumConst.ordinal()];
        } else {
            return action;
        }
    }

    public void setAction(TitleAction action) {
        if (packet != null) {
            Enum<?> enumConst = EnumUtil.valueByIndex(enumTitleActionClass, action.ordinal());
            writeEnumConstant(0, enumConst);
        } else {
            this.action = action;
        }
    }

    public String getText() {
        if (packet != null) {
            return readIChatBaseComponent(0);
        } else {
            return text;
        }
    }

    public void setText(String text) {
        if (packet != null) {
            writeIChatBaseComponent(0, text);
        } else {
            this.text = text;
        }
    }

    public int getFadeInTicks() {
        if (packet != null) {
            return readInt(0);
        } else {
            return fadeInTicks;
        }
    }

    public void setFadeInTicks(int fadeInTicks) {
        if (packet != null) {
            writeInt(0, fadeInTicks);
        }
        this.fadeInTicks = fadeInTicks;
    }

    public int getStayTicks() {
        if (packet != null) {
            return readInt(1);
        } else {
            return stayTicks;
        }
    }

    public void setStayTicks(int stayTicks) {
        if (packet != null) {
            writeInt(1, stayTicks);
        } else {
            this.stayTicks = stayTicks;
        }
    }

    public int getFadeOutTicks() {
        if (packet != null) {
            return readInt(2);
        } else {
            return fadeOutTicks;
        }
    }

    public void setFadeOutTicks(int fadeOutTicks) {
        if (packet != null) {

            writeInt(2, fadeOutTicks);
        } else {
            this.fadeOutTicks = fadeOutTicks;
        }
    }

    @Override
    public boolean isSupported() {
        return version.isNewerThan(ServerVersion.v_1_7_10) && version.isOlderThan(ServerVersion.v_1_17);
    }

    @Override
    public Object asNMSPacket() throws Exception {
        Enum<?> enumConst = EnumUtil.valueByIndex(enumTitleActionClass, getAction().ordinal());
        return packetConstructor.newInstance(enumConst, NMSUtils.generateIChatBaseComponent(getText()),
                getFadeInTicks(), getStayTicks(), getFadeOutTicks());
    }

    public enum TitleAction {
        TITLE,
        SUBTITLE,
        TIMES,
        CLEAR,
        RESET
    }
}
