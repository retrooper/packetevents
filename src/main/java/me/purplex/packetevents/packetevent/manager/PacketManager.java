package me.purplex.packetevents.packetevent.manager;

import me.purplex.packetevents.packetevent.PacketEvent;
import me.purplex.packetevents.packetevent.impl.ServerTickEvent;
import me.purplex.packetevents.packetevent.manager.PacketEventManager;
import me.purplex.packetevents.packetevent.handler.PacketHandler;
import me.purplex.packetevents.packetevent.listener.PacketListener;
import me.purplex.packetevents.packetevent.impl.PacketReceiveEvent;
import me.purplex.packetevents.packetevent.impl.PacketSendEvent;

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

}
