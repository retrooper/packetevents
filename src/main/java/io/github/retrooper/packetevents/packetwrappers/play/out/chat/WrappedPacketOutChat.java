/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.packetwrappers.play.out.chat;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public final class WrappedPacketOutChat extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> chatClassConstructor;
    private static Class<?> packetClass, iChatBaseComponentClass, chatMessageTypeEnum;
    private static Method chatMessageTypeCreatorMethod;
    //0 = IChatBaseComponent, Byte
    //1 = IChatBaseComponent, Int
    //2 = IChatBaseComponent, ChatMessageType
    //3 = IChatBaseComponent, ChatMessageType, UUID
    private static byte constructorMode;
    private String message;
    private ChatPosition chatPosition;
    private UUID uuid;

    public WrappedPacketOutChat(final NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutChat(String message, UUID uuid, boolean isJson) {
        this(message, ChatPosition.CHAT, uuid, isJson);
    }

    public WrappedPacketOutChat(BaseComponent component, UUID uuid) {
        this(component, ChatPosition.CHAT, uuid);
    }

    public WrappedPacketOutChat(BaseComponent component, ChatPosition pos, UUID uuid) {
        this(ComponentSerializer.toString(component), pos, uuid, true);
    }

    public WrappedPacketOutChat(String message, ChatPosition chatPosition, UUID uuid, boolean isJson) {
        this.uuid = uuid;
        this.message = isJson ? message : NMSUtils.fromStringToJSON(message);
        this.chatPosition = chatPosition;
    }

    @Override
    protected void load() {
        try {
            packetClass = PacketTypeClasses.Play.Server.CHAT;
            iChatBaseComponentClass = NMSUtils.getNMSClass("IChatBaseComponent");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        boolean isVeryOutdated = false;
        try {
            chatMessageTypeEnum = NMSUtils.getNMSClass("ChatMessageType");
        } catch (ClassNotFoundException e) {
            isVeryOutdated = true;
        }

        if (!isVeryOutdated) {
            chatMessageTypeCreatorMethod = Reflection.getMethod(chatMessageTypeEnum, 0, byte.class);

            try {
                chatClassConstructor = packetClass.getConstructor(iChatBaseComponentClass, chatMessageTypeEnum);
                constructorMode = 2;
            } catch (NoSuchMethodException e) {
                //Just a much newer version(1.16.x and above right now)
                try {
                    chatClassConstructor = packetClass.getConstructor(iChatBaseComponentClass, chatMessageTypeEnum, UUID.class);
                    constructorMode = 3;
                } catch (NoSuchMethodException e2) {
                    //Failed to resolve the constructor
                    e2.printStackTrace();
                }
            }
        } else {
            try {
                chatClassConstructor = packetClass.getConstructor(iChatBaseComponentClass, byte.class);
                constructorMode = 0;
            } catch (NoSuchMethodException e) {
                //That is fine, they are most likely on an older version.
                try {
                    chatClassConstructor = packetClass.getConstructor(iChatBaseComponentClass, int.class);
                    constructorMode = 1;
                } catch (NoSuchMethodException e2) {
                    try {
                        //Some weird 1.7.10 spigots remove that int parameter for no reason, I won't keep adding support for any more spigots and might stop
                        //accepting pull requests for support for spigots breaking things that normal spigot has.
                        chatClassConstructor = packetClass.getConstructor(iChatBaseComponentClass);
                        constructorMode = -1;
                    } catch (NoSuchMethodException noSuchMethodException) {
                        noSuchMethodException.printStackTrace();
                    }
                }
            }
        }

    }

    @Override
    public Object asNMSPacket() {
        byte chatPos = (byte) getChatPosition().ordinal();
        Object chatMessageTypeInstance = null;
        if (chatMessageTypeEnum != null) {
            try {
                chatMessageTypeInstance = chatMessageTypeCreatorMethod.invoke(null, chatPos);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        switch (constructorMode) {
            case -1:
                try {
                    return chatClassConstructor.newInstance(NMSUtils.generateIChatBaseComponent(getMessage()));
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            case 0:
                try {
                    return chatClassConstructor.newInstance(NMSUtils.generateIChatBaseComponent(getMessage()), chatPos);
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    return chatClassConstructor.newInstance(NMSUtils.generateIChatBaseComponent(getMessage()), (int) chatPos);
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    return chatClassConstructor.newInstance(NMSUtils.generateIChatBaseComponent(getMessage()), chatMessageTypeInstance);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    return chatClassConstructor.newInstance(NMSUtils.generateIChatBaseComponent(getMessage()), chatMessageTypeInstance, uuid);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
        }
        return null;
    }

    /**
     * Get the message.
     *
     * @return Get String Message
     */
    public String getMessage() {
        if (packet != null) {
            final Object iChatBaseObj = readObject(0, iChatBaseComponentClass);
            return NMSUtils.readIChatBaseComponent(iChatBaseObj);
        } else {
            return message;
        }
    }

    /**
     * Get the chat position.
     * <p>
     * On 1.7.10, Only CHAT and SYSTEM_MESSAGE exist.
     * If an invalid chat position is sent, it will be defaulted it to CHAT.
     *
     * @return ChatPosition
     */
    public ChatPosition getChatPosition() {
        if (packet != null) {
            byte chatPositionValue;
            switch (constructorMode) {
                case -1:
                    chatPositionValue = (byte) ChatPosition.CHAT.ordinal();
                    break;
                case 0:
                    chatPositionValue = readByte(0);
                    break;
                case 1:
                    chatPositionValue = (byte) readInt(0);
                    break;
                case 2:
                case 3:
                    Object chatTypeEnumInstance = readObject(0, chatMessageTypeEnum);
                    return ChatPosition.valueOf(chatTypeEnumInstance.toString());
                default:
                    chatPositionValue = 0;
                    break;
            }
            return ChatPosition.getByPositionValue(chatPositionValue);
        } else {
            return chatPosition;
        }
    }

    public enum ChatPosition {
        CHAT,
        SYSTEM_MESSAGE,
        GAME_INFO;

        ChatPosition() {
        }

        public static ChatPosition getByPositionValue(byte position) {
            return values()[position];
        }
    }
}
