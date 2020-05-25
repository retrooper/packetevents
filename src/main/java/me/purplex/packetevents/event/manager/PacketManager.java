package me.purplex.packetevents.event.manager;

import me.purplex.packetevents.event.PacketEvent;
import me.purplex.packetevents.event.handler.PacketHandler;
import me.purplex.packetevents.event.listener.PacketListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PacketManager implements PacketEventManager {
    @Override
    public void callEvent(PacketEvent e) {
        for (PacketListener listener : this.packetListeners) {
            Method[] methods = listener.getClass().getMethods();
            for (Method m : methods) {
                if (m.isAnnotationPresent(PacketHandler.class)) {

                    if (m.getParameterTypes()[0].getSimpleName().equals(e.getClass().getSimpleName())) {
                        try {
                            m.invoke(listener, e);
                        } catch (IllegalAccessException ex) {
                            ex.printStackTrace();
                        } catch (InvocationTargetException ex) {
                            ex.printStackTrace();
                        }
                    }

                }
            }
        }
    }

    @Override
    public void registerListener(PacketListener e) {
        this.packetListeners.add(e);
    }

    @Override
    public void unregisterListener(PacketListener e) {
        this.packetListeners.remove(e);
    }

    @Override
    public void unregisterAllListeners(PacketListener e) {
        this.packetListeners.clear();
    }

    /*@Override
    public void sendPacket(Player player, WrappedPacket packet) {

    }*/

}
