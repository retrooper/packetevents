package io.github.retrooper.packetevents.packetwrappers.out.playerinfo;

import io.github.retrooper.packetevents.enums.minecraft.Gamemode;
import io.github.retrooper.packetevents.enums.minecraft.PlayerInfoAction;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;

public class WrappedPacketOutPlayerInfo extends WrappedPacket {
    private PlayerInfoAction action;

    public WrappedPacketOutPlayerInfo(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        PacketPlayOutPlayerInfo.PlayerInfoData dl;
    }

    private static Class<?> packetClass, enumPlayerInfoActionClass;

    static {
        try {
            packetClass = NMSUtils.getNMSClass("PacketPlayOutPlayerInfo");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            enumPlayerInfoActionClass = NMSUtils.getNMSClass("EnumPLayerInfoAction");
        } catch (ClassNotFoundException e) {
            enumPlayerInfoActionClass = Reflection.getSubClass(packetClass, "EnumPlayerInfoAction");
        }
    }

    public class PlayerDataInfo {
        public PlayerDataInfo(Object gameProfile, int var, Gamemode gamemode,String s ) {

        }
    }

}
