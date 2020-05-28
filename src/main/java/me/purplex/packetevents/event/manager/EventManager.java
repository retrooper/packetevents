package me.purplex.packetevents.event.manager;

import me.purplex.packetevents.event.PacketEvent;
import me.purplex.packetevents.event.handler.PacketHandler;
import me.purplex.packetevents.event.listener.PacketListener;

import java.lang.reflect.*;

public class EventManager implements PacketEventManager {

    @Override
    public void callEvent(final PacketEvent e) {
        for (final PacketListener listener : this.packetListeners) {
            boolean calledOnPacket = false;
            //Annotated methods
            final Method[] methods = listener.getClass().getMethods();
            for (int i = 0; i < methods.length; i++) {
                final Method m = methods[i];
                if (m.isAnnotationPresent(PacketHandler.class)) {
                    if (!calledOnPacket && m.getParameterTypes()[0].equals(PacketEvent.class)) {
                        try {
                            m.invoke(listener, e);
                        } catch (IllegalAccessException ex) {
                            ex.printStackTrace();
                        } catch (InvocationTargetException ex) {
                            ex.printStackTrace();
                        }
                        calledOnPacket = true;
                    } else if (m.getParameterTypes()[0].getSimpleName().equals(e.getClass().getSimpleName())) {
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
    public boolean unregisterListener(final PacketListener e) {
       return this.packetListeners.remove(e);
    }

    @Override
    public boolean unregisterAllListeners() {
        if (this.packetListeners.size() != 0) {
            this.packetListeners.clear();
            return true;
        }
        return false;
    }

    /*@Override
    public void sendPacket(Player player, WrappedPacket packet) {

    }*/

}
