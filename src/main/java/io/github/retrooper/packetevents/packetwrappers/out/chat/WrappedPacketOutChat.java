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

package io.github.retrooper.packetevents.packetwrappers.out.chat;

import io.github.retrooper.packetevents.annotations.Beta;
import io.github.retrooper.packetevents.annotations.NotNull;
import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.UUID;

/**
 * This wrapper MIGHT not be stable
 */
@Beta
public final class WrappedPacketOutChat extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> chatClassConstructor;
    private static Class<?> packetClass, iChatBaseComponentClass, chatSerializerClass, chatMessageTypeEnum;
    private static Method chatMessageTypeCreatorMethod;
    //0 = IChatBaseComponent, Byte
    //1 = IChatBaseComponent, Int
    //2 = IChatBaseComponent, ChatMessageType
    //3 = IChatBaseComponent, ChatMessageType, UUID
    private static byte constructorMode;

    //0 = Byte
    //1 = Byte, Boolean
    private static byte chatTypeMessageEnumConstructorMode;
    private static final HashMap<ChatPosition, Byte> cachedChatPositions = new HashMap<ChatPosition, Byte>();
    private static final HashMap<Byte, ChatPosition> cachedChatPositionIntegers = new HashMap<Byte, ChatPosition>();
    private static final HashMap<String, Byte> cachedChatMessageTypeIntegers = new HashMap<String, Byte>();
    private String message;
    private ChatPosition chatPosition;
    private UUID uuid;


    @Deprecated
    public WrappedPacketOutChat(String message) {
        this(message, null);
    }

    public WrappedPacketOutChat(final Object packet) {
        super(packet);
    }

    public WrappedPacketOutChat(String message, UUID uuid) {
        this(message, ChatPosition.CHAT, uuid);
    }

    public WrappedPacketOutChat(String message, ChatPosition chatPosition, UUID uuid) {
        this.uuid = uuid;
        this.message = message;
        this.chatPosition = chatPosition;
    }

    public enum ChatPosition {
        CHAT, SYSTEM_MESSAGE, GAME_INFO
    }


    public static void load() {
        try {
            packetClass = PacketTypeClasses.Server.CHAT;
            iChatBaseComponentClass = NMSUtils.getNMSClass("IChatBaseComponent");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //In 1.8.3+ the ChatSerializer class is declared in the IChatBaseComponent class, so we have to handle that
        try {
            chatSerializerClass = NMSUtils.getNMSClass("ChatSerializer");
        } catch (ClassNotFoundException e) {
            //That is fine, it is probably a subclass
            chatSerializerClass = SubclassUtil.getSubClass(iChatBaseComponentClass, "ChatSerializer");
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
                    e2.printStackTrace();
                }
            }
        }

        cachedChatPositions.put(ChatPosition.CHAT, (byte) 0);
        cachedChatPositions.put(ChatPosition.SYSTEM_MESSAGE, (byte) 1);
        cachedChatPositions.put(ChatPosition.GAME_INFO, (byte) 2);

        cachedChatPositionIntegers.put((byte) 0, ChatPosition.CHAT);
        cachedChatPositionIntegers.put((byte) 1, ChatPosition.SYSTEM_MESSAGE);
        cachedChatPositionIntegers.put((byte) 2, ChatPosition.GAME_INFO);


        cachedChatMessageTypeIntegers.put("CHAT", (byte) 0);
        cachedChatMessageTypeIntegers.put("SYSTEM", (byte) 1);
        cachedChatMessageTypeIntegers.put("GAME_INFO", (byte) 2);

    }

    public static String fromStringToJSON(String message) {
        return "{\"text\": \"" + message + "\"}";
    }

    public static String toStringFromIChatBaseComponent(Object obj) {
        try {
            return Reflection.getMethod(iChatBaseComponentClass, String.class, 0).invoke(obj).toString();
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object toIChatBaseComponent(String msg) {
        try {
            return Reflection.getMethod(chatSerializerClass, 0, String.class).invoke(null, msg);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void setup() {
        try {
            final Object iChatBaseObj = readObject(0, iChatBaseComponentClass);

            final Object contentString = Reflection.getMethod(iChatBaseComponentClass, String.class, 0).invoke(iChatBaseObj);

            this.message = contentString.toString();

            byte chatPosInteger = 0;
            switch (constructorMode) {
                case 0:
                    chatPosInteger = readByte(0);
                    break;
                case 1:
                    chatPosInteger = (byte) readInt(0);
                    break;
                case 2:
                case 3:
                    Object chatTypeEnumInstance = readObject(0, chatMessageTypeEnum);
                    chatPosInteger = cachedChatMessageTypeIntegers.get(chatTypeEnumInstance.toString());
                    break;
            }
            this.chatPosition = cachedChatPositionIntegers.get(chatPosInteger);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object asNMSPacket() {
        int integerChatPos = cachedChatPositions.get(chatPosition);
        Object chatMessageTypeInstance = null;
        if (chatMessageTypeEnum != null) {
            if (chatTypeMessageEnumConstructorMode == 0) {
                try {
                    chatMessageTypeInstance = chatMessageTypeCreatorMethod.invoke(null, (byte) integerChatPos);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else if (chatTypeMessageEnumConstructorMode == 1) {
                try {
                    chatMessageTypeInstance = chatMessageTypeCreatorMethod.invoke(null, (byte) integerChatPos);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        switch (constructorMode) {
            case 0:
                try {
                    return chatClassConstructor.newInstance(toIChatBaseComponent(this.message), (byte) integerChatPos);
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    return chatClassConstructor.newInstance(toIChatBaseComponent(this.message), integerChatPos);
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    return chatClassConstructor.newInstance(toIChatBaseComponent(this.message), chatMessageTypeInstance);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    return chatClassConstructor.newInstance(toIChatBaseComponent(this.message), chatMessageTypeInstance, uuid);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
        }
        return null;
    }

    /**
     * Get the message.
     * @return Get String Message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Get the chat position.
     *
     * On 1.7.10, Only CHAT and SYSTEM_MESSAGE exist.
     * If an invalid chat position is sent, it will be defaulted it to CHAT.
     * @return ChatPosition
     */
    @NotNull
    public ChatPosition getChatPosition() {
        if(this.chatPosition == null) {
            this.chatPosition = ChatPosition.CHAT;
        }
        return this.chatPosition;
    }
}
