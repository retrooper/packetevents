package io.github.retrooper.packetevents.packetwrappers.play.out.playerinfo;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.gameprofile.GameProfileUtil;
import io.github.retrooper.packetevents.utils.gameprofile.WrappedGameProfile;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.GameMode;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

//TODO finish wrapper
public class WrappedPacketOutPlayerInfo extends WrappedPacket implements SendableWrapper {
    private static Class<? extends Enum<?>> enumPlayerInfoActionClass;
    private static Class<?> playerInfoDataClass;
    private static Constructor<?> packetDefaultConstructor, playerInfoDataConstructor;
    private static byte constructorMode = 0;
    private PlayerInfoAction action;
    private List<PlayerInfo> playerInfoList = new ArrayList<>();

    public WrappedPacketOutPlayerInfo(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutPlayerInfo(PlayerInfoAction action, List<PlayerInfo> playerInfoList) {
        this.action = action;
        this.playerInfoList = playerInfoList;
    }

    @Override
    protected void load() {
        enumPlayerInfoActionClass = SubclassUtil.getEnumSubClass(PacketTypeClasses.Play.Server.PLAYER_INFO, "EnumPlayerInfoAction");
        try {
            packetDefaultConstructor = PacketTypeClasses.Play.Server.PLAYER_INFO.getConstructor();
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        playerInfoDataClass = SubclassUtil.getSubClass(PacketTypeClasses.Play.Server.PLAYER_INFO, "PlayerInfoData");
        if (playerInfoDataClass != null) {
            try {
                playerInfoDataConstructor = playerInfoDataClass.getConstructor(NMSUtils.gameProfileClass, int.class, NMSUtils.enumGameModeClass, NMSUtils.iChatBaseComponentClass);
            } catch (NoSuchMethodException e) {
                try {
                    playerInfoDataConstructor = playerInfoDataClass.getConstructor(PacketTypeClasses.Play.Server.PLAYER_INFO, NMSUtils.gameProfileClass, int.class, NMSUtils.enumGameModeClass, NMSUtils.iChatBaseComponentClass);
                    constructorMode = 1;
                } catch (NoSuchMethodException e2) {
                    e.printStackTrace();
                    e2.printStackTrace();
                }

            }
        }
    }

    public PlayerInfoAction getAction() {
        if (packet != null) {
            int index;
            if (version.isOlderThan(ServerVersion.v_1_8)) {
                index = readInt(0);
            } else {
                Enum<?> enumConst = readEnumConstant(0, enumPlayerInfoActionClass);
                index = enumConst.ordinal();
            }
            return PlayerInfoAction.values()[index];
        } else {
            return action;
        }
    }

    public void setAction(PlayerInfoAction action) {
        if (packet != null) {
            int index = action.ordinal();
            if (version.isOlderThan(ServerVersion.v_1_8)) {
                writeInt(0, index);
            } else {
                Enum<?> enumConst = EnumUtil.valueByIndex(enumPlayerInfoActionClass, action.ordinal());
                writeEnumConstant(0, enumConst);
            }
        } else {
            this.action = action;
        }
    }

    public List<PlayerInfo> getPlayerInfo() {
        if (packet != null) {
            List<PlayerInfo> playerInfoList = new ArrayList<>();
            if (version.isOlderThan(ServerVersion.v_1_8)) {
                String username = readString(0);
                Object mojangGameProfile = readObject(0, NMSUtils.gameProfileClass);
                WrappedGameProfile gameProfile = GameProfileUtil.getWrappedGameProfile(mojangGameProfile);
                GameMode gameMode = GameMode.values()[readInt(1)];
                int ping = readInt(2);
                playerInfoList.add(new PlayerInfo(username, gameProfile, gameMode, ping));
            } else {
                List<Object> nmsPlayerInfoDataList = readObject(0, List.class);
                for (Object nmsPlayerInfoData : nmsPlayerInfoDataList) {
                    WrappedPacket nmsPlayerInfoDataWrapper = new WrappedPacket(new NMSPacket(nmsPlayerInfoData));
                    Object iChatBaseComponentName = nmsPlayerInfoDataWrapper.readObject(0, NMSUtils.iChatBaseComponentClass);
                    String username;
                    if (iChatBaseComponentName== null) {
                        username = null;
                    }
                    else {
                        username = NMSUtils.readIChatBaseComponent(iChatBaseComponentName);
                    }
                    Object mojangGameProfile = nmsPlayerInfoDataWrapper.readObject(0, NMSUtils.gameProfileClass);
                    WrappedGameProfile gameProfile = GameProfileUtil.getWrappedGameProfile(mojangGameProfile);
                    Enum<?> nmsGameModeEnumConstant = nmsPlayerInfoDataWrapper.readEnumConstant(0, NMSUtils.enumGameModeClass);
                    GameMode gameMode = GameMode.valueOf(nmsGameModeEnumConstant.name());
                    int ping = nmsPlayerInfoDataWrapper.readInt(0);
                    playerInfoList.add(new PlayerInfo(username, gameProfile, gameMode, ping));
                }
            }
            return playerInfoList;
        } else {
            return playerInfoList;
        }
    }

    public void setPlayerInfo(List<PlayerInfo> playerInfoList) throws UnsupportedOperationException {
        if (version.isOlderThan(ServerVersion.v_1_8) && playerInfoList.size() > 1) {
            throw new UnsupportedOperationException("The player info list size cannot be greater than 1 one 1.7.10 server versions!");
        }
        if (packet != null) {
            if (version.isOlderThan(ServerVersion.v_1_8)) {
                PlayerInfo playerInfo = playerInfoList.get(0);
                writeString(0, playerInfo.username);
                Object mojangGameProfile = GameProfileUtil.getGameProfile(playerInfo.gameProfile.id, playerInfo.gameProfile.name);
                writeObject(0, mojangGameProfile);
                writeInt(1, playerInfo.gameMode.ordinal());
                writeInt(2, playerInfo.ping);
            } else {
                List<Object> nmsPlayerInfoList = new ArrayList<>();

                for (PlayerInfo playerInfo : playerInfoList) {
                    Object usernameIChatBaseComponent = NMSUtils.generateIChatBaseComponent(NMSUtils.fromStringToJSON(playerInfo.username));
                    Object mojangGameProfile = GameProfileUtil.getGameProfile(playerInfo.gameProfile.id, playerInfo.gameProfile.name);
                    Enum<?> nmsGameModeEnumConstant = EnumUtil.valueOf(NMSUtils.enumGameModeClass, playerInfo.gameMode.name());
                    int ping = playerInfo.ping;
                    try {
                        if (constructorMode == 0) {
                            nmsPlayerInfoList.add(playerInfoDataConstructor.newInstance(mojangGameProfile, ping, nmsGameModeEnumConstant, usernameIChatBaseComponent));

                        } else if (constructorMode == 1) {
                            nmsPlayerInfoList.add(playerInfoDataConstructor.newInstance(null, mojangGameProfile, ping, nmsGameModeEnumConstant, usernameIChatBaseComponent));
                        }
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                write(List.class, 0, nmsPlayerInfoList);
            }
        } else {
            this.playerInfoList = playerInfoList;
        }
    }

    @Override
    public Object asNMSPacket() {
        try {
            Object packetPlayOutPlayerInfoInstance = packetDefaultConstructor.newInstance();
            WrappedPacketOutPlayerInfo playerInfoWrapper = new WrappedPacketOutPlayerInfo(new NMSPacket(packetPlayOutPlayerInfoInstance));
            List<PlayerInfo> playerInfos = getPlayerInfo();
            if (!playerInfos.isEmpty()) {
                playerInfoWrapper.setPlayerInfo(playerInfos);
            }
            playerInfoWrapper.setAction(getAction());
            return packetPlayOutPlayerInfoInstance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public enum PlayerInfoAction {
        ADD_PLAYER,
        UPDATE_GAME_MODE,
        UPDATE_LATENCY,
        UPDATE_DISPLAY_NAME,
        REMOVE_PLAYER
    }

    public static class PlayerInfo {
        private String username;
        private WrappedGameProfile gameProfile;
        private GameMode gameMode;
        private int ping;

        public PlayerInfo(@Nullable String username, WrappedGameProfile gameProfile, GameMode gameMode, int ping) {
            this.username = username;
            this.gameProfile = gameProfile;
            this.gameMode = gameMode;
            this.ping = ping;
        }

        public WrappedGameProfile getGameProfile() {
            return gameProfile;
        }

        public void setGameProfile(WrappedGameProfile gameProfile) {
            this.gameProfile = gameProfile;
        }

        public GameMode getGameMode() {
            return gameMode;
        }

        public void setGameMode(GameMode gameMode) {
            this.gameMode = gameMode;
        }

        public int getPing() {
            return ping;
        }

        public void setPing(int ping) {
            this.ping = ping;
        }

        @Nullable
        public String getUsername() {
            return username;
        }

        public void setUsername(@Nullable String username) {
            this.username = username;
        }
    }
}
