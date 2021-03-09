package io.github.retrooper.packetevents.packetwrappers.play.out.viewdistance;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class WrappedPacketOutViewDistance extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> packetConstructor;
    private int viewDistance;

    public WrappedPacketOutViewDistance(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        try {
            packetConstructor = PacketTypeClasses.Play.Server.VIEW_DISTANCE.getConstructor(int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public int getViewDistance() {
        if (packet != null) {
            return readInt(0);
        } else {
            return viewDistance;
        }
    }

    public void setViewDistance(int viewDistance) {
        if (packet != null) {
            writeInt(0, viewDistance);
        } else {
            this.viewDistance = viewDistance;
        }
    }

    @Override
    public Object asNMSPacket() {
        try {
            return packetConstructor.newInstance(getViewDistance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isSupported() {
        return version.isNewerThan(ServerVersion.v_1_13_2); //TODO civ
    }
}
