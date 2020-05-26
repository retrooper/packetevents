package me.purplex.packetevents.event.manager;

import me.purplex.packetevents.event.PacketEvent;
import me.purplex.packetevents.event.handler.PacketHandler;
import me.purplex.packetevents.event.listener.PacketListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PacketManager implements PacketEventManager {

    @Override
    public void callEvent(final PacketEvent e) {
        boolean calledOnPacket = false;
        for (final PacketListener listener : this.packetListeners) {
            final Method[] methods = listener.getClass().getMethods();
            for (final Method m : methods) {
                if (m.isAnnotationPresent(PacketHandler.class)) {
                    if(!calledOnPacket && m.getParameterTypes()[0].getSimpleName().equals(PacketEvent.class.getSimpleName())) {
                        calledOnPacket = true;
                        try {
                            m.invoke(listener, e);
                        } catch (IllegalAccessException ex) {
                            ex.printStackTrace();
                        } catch (InvocationTargetException ex) {
                            ex.printStackTrace();
                        }
                    }
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
    public void registerListener(final PacketListener e) {
        this.packetListeners.add(e);
    }

    @Override
    public void unregisterListener(final PacketListener e) {
        this.packetListeners.remove(e);
    }

    @Override
    public void unregisterAllListeners(final PacketListener e) {
        this.packetListeners.clear();
    }

    /*@Override
    public void sendPacket(Player player, WrappedPacket packet) {

    }*/

}
