/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.retrooper.packetevents;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;

public final class PacketEvents {
    private static PacketEventsAPI API;

 //   private final InternalBukkitListener internalBukkitListener = new InternalBukkitListener();
 //   private final GlobalChannelInjector injector = new GlobalChannelInjector();
    public static String IDENTIFIER, ENCODER_NAME, DECODER_NAME, CONNECTION_NAME, SERVER_CHANNEL_HANDLER_NAME;
    //private UpdateChecker updateChecker;
    private static boolean LOADED;
    private static boolean INITIALIZED;
    private static boolean LATE_BIND = false;


    public static PacketEventsAPI getAPI() {
        return API;
    }

    //Plugin plugin
    public static void load(PacketEventsAPI api) {
        API = api;
        /*if (!loaded) {
            //Resolve server version and cache
            identifier = "pe-" + plugin.getName().toLowerCase();
            encoderName = "pe-encoder-" + plugin.getName().toLowerCase();
            decoderName = "pe-decoder-" + plugin.getName().toLowerCase();
            connectionName = "pe-connection-handler-" + plugin.getName().toLowerCase();
            serverChannelHandlerName = "pe-connection-initializer-" + plugin.getName().toLowerCase();
            try {
                MinecraftReflectionUtil.init();

                ServerManager.ENTITY_ID_CACHE = DependencyUtil.makeMap();
            } catch (Exception ex) {
                throw new PacketEventsLoadFailureException(ex);
            }

            updateChecker = new UpdateChecker();

            injector.load();
            lateBind = !injector.isBound();
            //If late-bind is enabled or ViaVersion is present, we will inject a bit later.
            if (!lateBind) {
                injector.inject();
            }

            loaded = true;

            //Register internal packet listener (should be the first listener)
            getEventManager().registerListener(new InternalPacketListener(), PacketListenerPriority.LOWEST);
        }*/
    }

    public static void init() {
        init(getAPI().getSettings());
    }

    public static void init(PacketEventsSettings packetEventsSettings) {
        //Load if we haven't loaded already
        //load(plugin);
        /*
        if (!initialized) {
            settings = packetEventsSettings;

            if (settings.shouldCheckForUpdates()) {
                handleUpdateCheck(plugin);
            }

            if (settings.isbStatsEnabled()) {
                Metrics metrics = new Metrics((JavaPlugin) plugin, 11327);
            }

            PacketType.Play.Client.load();
            PacketType.Play.Server.load();

            Runnable postInjectTask = () -> {
                Bukkit.getPluginManager().registerEvents(internalBukkitListener, plugin);
                for (final Player p : Bukkit.getOnlinePlayers()) {
                    try {
                        injector.injectPlayer(p);
                        getEventManager().callEvent(new PostPlayerInjectEvent(p));
                    } catch (Exception ex) {
                        p.kickPlayer("Failed to inject... Please rejoin!");
                    }
                }
            };

            if (lateBind) {
                //If late-bind is enabled, we still need to inject (after all plugins enabled).
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, injector::inject);
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, postInjectTask);
            } else {
                postInjectTask.run();
            }

            initialized = true;
        }*/
    }

    public static void terminate() {
        if (INITIALIZED) {
            /*
            //Eject all players
            for (Player p : Bukkit.getOnlinePlayers()) {
                injector.ejectPlayer(p);
            }
            //Eject the injector if needed
            injector.eject();
            //Unregister all our listeners
            getEventManager().unregisterAllListeners();
            initialized = false;
            */
        }
    }

    public static boolean hasLoaded() {
        return LOADED;
    }

    public static boolean isINITIALIZED() {
        return INITIALIZED;
    }

   // public GlobalChannelInjector getInjector() {
   //     return injector;
   // }

   /*
    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    private void handleUpdateCheck(Plugin plugin) {
        if (updateChecker == null) {
            updateChecker = new UpdateChecker();
        }
        Thread thread = new Thread(() -> {
            plugin.getLogger().info("[packetevents] Checking for an update, please wait...");
            UpdateChecker.UpdateCheckerStatus status = updateChecker.checkForUpdate();
            int waitTimeInSeconds = 5;
            int maxRetryCount = 5;
            int retries = 0;
            while (retries < maxRetryCount) {
                if (status != UpdateChecker.UpdateCheckerStatus.FAILED) {
                    break;
                }
                plugin.getLogger().severe("[packetevents] Checking for an update again in " + waitTimeInSeconds + " seconds...");
                try {
                    Thread.sleep(waitTimeInSeconds * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                waitTimeInSeconds *= 2;

                status = updateChecker.checkForUpdate();

                if (retries == (maxRetryCount - 1)) {
                    plugin.getLogger().severe("[packetevents] PacketEvents failed to check for an update. No longer retrying.");
                    break;
                }

                retries++;
            }

        }, "packetevents-update-check-thread");
        thread.start();
    }*/

}
