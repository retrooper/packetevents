package me.purplex.packetevents.packetmanager;

import me.purplex.packetevents.events.PacketEvent;
import me.purplex.packetevents.events.packetevent.ServerTickEvent;
import me.purplex.packetevents.eventmanager.PacketEventManager;
import me.purplex.packetevents.events.handler.PacketHandler;
import me.purplex.packetevents.events.listener.PacketListener;
import me.purplex.packetevents.events.packetevent.PacketReceiveEvent;
import me.purplex.packetevents.events.packetevent.PacketSendEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PacketManager implements PacketEventManager {
    @Override
    public void callEvent(PacketEvent e) {
        for (PacketListener listener : this.packetListeners) {
            Method[] methods = listener.getClass().getMethods();
            for (Method m : methods) {
                if (m.isAnnotationPresent(PacketHandler.class)) {
                    if(e instanceof PacketReceiveEvent) {
                        PacketReceiveEvent event = (PacketReceiveEvent)e;
                        if(m.getParameterTypes()[0].getSimpleName().equals(event.getClass().getSimpleName())) {
                            try {
                                m.invoke(listener, event);
                            } catch (IllegalAccessException ex) {
                                ex.printStackTrace();
                            } catch (InvocationTargetException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    else if(e instanceof PacketSendEvent) {
                        PacketSendEvent event = (PacketSendEvent)e;
                        if(m.getParameterTypes()[0].getSimpleName().equals(event.getClass().getSimpleName())) {
                            try {
                                m.invoke(listener, event);
                            } catch (IllegalAccessException ex) {
                                ex.printStackTrace();
                            } catch (InvocationTargetException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    else if(e instanceof ServerTickEvent) {
                        ServerTickEvent event = (ServerTickEvent)e;
                        if(m.getParameterTypes()[0].getSimpleName().equals(event.getClass().getSimpleName())) {
                            try {
                                m.invoke(listener, event);
                            } catch (IllegalAccessException ex) {
                                ex.printStackTrace();
                            } catch (InvocationTargetException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    else {
                        if(m.getParameterTypes()[0].getSimpleName().equals(e.getClass().getSimpleName())) {
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
