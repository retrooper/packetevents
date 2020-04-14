package me.retrooper.packetevents.events;

import java.lang.reflect.Method;

public class PacketManager implements PacketEventManager {
    @Override
    public void callPacketReceiveEvent(PacketReceiveEvent e) {
        for (PacketListener listener : this.packetListeners) {
            Method[] methods = listener.getClass().getMethods();
            for (Method m : methods) {
                if (m.isAnnotationPresent(PacketHandler.class)) {
                    if (m.getParameterTypes()[0].getSimpleName().equals(e.getClass().getSimpleName())) {
                        try {
                            m.invoke(listener, e);
                        } catch (Exception ex) {

                        }
                    }
                }
            }
        }
    }


    @Override
    public void callPacketSendEvent(PacketSendEvent e) {
        for (PacketListener listener : this.packetListeners) {
            Method[] methods = listener.getClass().getMethods();
            for (Method m : methods) {
                if (m.isAnnotationPresent(PacketHandler.class)) {
                    if (m.getParameterTypes()[0].getSimpleName().equals(e.getClass().getSimpleName())) {
                        try {
                            m.invoke(listener, e);
                        } catch (Exception ex) {

                        }
                    }
                }
            }
        }
    }

    @Override
    public void registerPacketListener(PacketListener e) {
        this.packetListeners.add(e);
    }
}
