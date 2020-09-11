/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents;

import io.github.retrooper.packetevents.api.PacketEventsAPI;
import io.github.retrooper.packetevents.enums.ClientVersion;
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.impl.BukkitMoveEvent;
import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.utils.NMSUtils;
import io.github.retrooper.packetevents.utils.nms_entityfinder.EntityFinderUtils;
import io.github.retrooper.packetevents.utils.versionlookup.VersionLookupUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;

public final class PacketEvents implements Listener {
    private static final PacketEventsAPI packetEventsAPI = new PacketEventsAPI();
    private static final PacketEvents instance = new PacketEvents();
    private static final PacketEventsSettings settings = new PacketEventsSettings();
    private static final ArrayList<Plugin> plugins = new ArrayList<Plugin>(1);
    private static boolean hasLoaded, isStarted, hasCreatedTempDataFile;

    /**
     * This loads the PacketEvents API.
     * <p>
     * ServerVersion:
     * In this method we detect and cache the server version.
     * <p>
     * NMSUtils:
     * We setup some NMS utilities.
     * <p>
     * Packet ID System:
     * All the packet classes we will be needing are cached in a Map with an integer ID.
     * <p>
     * Version Lookup Utils:
     * We setup the client protocol version system.
     * We check if ViaVersion, ProtocolSupport or ProtocolLib is present.
     * <p>
     * Wrappers:
     * All PacketEvents' wrappers are setup and do all loading they need to do.
     */
    public static void load() {
        ServerVersion version = ServerVersion.getVersion();
        WrappedPacket.version = version;
        PacketEvent.version = version;
        NMSUtils.version = version;
        EntityFinderUtils.version = version;

        NMSUtils.load();

        PacketTypeClasses.Client.load();
        PacketTypeClasses.Server.load();

        EntityFinderUtils.load();

        ClientVersion.prepareLookUp();

        WrappedPacket.loadAllWrappers();
        hasLoaded = true;
    }

    /**
     * Initiates PacketEvents
     * <p>
     * Loading:
     * Loads PacketEvents if you haven't already.
     * <p>
     * Registering:
     * Registers this class as a Bukkit listener to inject/eject players.
     *
     * @param pl JavaPlugin instance
     */
    public static void init(final Plugin pl) {
        if (!hasLoaded) {
            load();
        }
        if (!isStarted) {
            plugins.add(pl);
            //Register Bukkit listener
            Bukkit.getPluginManager().registerEvents(instance, plugins.get(0));

            for (final Player p : Bukkit.getOnlinePlayers()) {
                getAPI().getPlayerUtils().injectPlayer(p);
            }
            isStarted = true;
        }

        if (!hasCreatedTempDataFile) {
            File tempDataFile = getTemporaryDataFile();
            if (!tempDataFile.exists()) {
                boolean success = tempDataFile.mkdir();
                if (!success) {
                    Bukkit.getConsoleSender().sendMessage(
                            ChatColor.RED + "PacketEvents failed to create a temporary data file.");
                    return;
                }
            } else {
                String data = readFromTemporaryDataFile(tempDataFile);
                if(data.equals("false shutdown")) {
                    tempDataFile.delete();
                    return;
                }
                else if (data.equals("true")) {
                    Bukkit.getConsoleSender().sendMessage(
                            ChatColor.YELLOW + "PacketEvents has found another instance of PacketEvents," +
                                    " it is recommended to install ProtocolLib to ensure no bugs occur.");
                } else if (data.equals("false")) {
                    writeToTempDataFile("true", tempDataFile);
                }
            }

            hasCreatedTempDataFile = true;
        }
    }

    /**
     * Loads PacketEvents if you haven't already, Sets everything up, injects all players
     *
     * @param pl Plugin instance
     * @deprecated Use {@link #init(Plugin)}
     */
    @Deprecated
    public static void start(final Plugin pl) {
        init(pl);
    }

    /**
     *
     */
    public static void stop() {
        if (isStarted) {
            for (final Player p : Bukkit.getOnlinePlayers()) {
                getAPI().getPlayerUtils().ejectPlayer(p);
            }
            getAPI().getEventManager().unregisterAllListeners();

            isStarted = false;
            File tempDataFile = getTemporaryDataFile();
            if (tempDataFile.exists()) {
                writeToTempDataFile("false shutdown", tempDataFile);
            }
        }
    }

    public static boolean hasLoaded() {
        return hasLoaded;
    }

    @Deprecated
    public static boolean hasStarted() {
        return isStarted;
    }

    public static boolean isInitialized() {
        return isStarted;
    }

    public static PacketEventsAPI getAPI() {
        return packetEventsAPI;
    }

    @Deprecated
    public static Plugin getPlugin() {
        return plugins.get(0);
    }

    public static ArrayList<Plugin> getPlugins() {
        return plugins;
    }

    public static String getHandlerName(final String name) {
        return "pe-" + settings.getIdentifier() + "-" + name;
    }

    public static PacketEventsSettings getSettings() {
        return settings;
    }

    private static File getTemporaryDataFile() {
        File file = new File(".");
        String path = "";
        try {
            path = file.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        path += File.separator + "plugins" + File.separator + "packeteventsdata.tmp";
        return new File(path);
    }

    private static String readFromTemporaryDataFile(File tempDataFile) {
        String data = "";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(tempDataFile))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                line = bufferedReader.readLine();
                data += line;
            }
        } catch (FileNotFoundException e) {
            Bukkit.getConsoleSender().sendMessage(
                    ChatColor.RED + "PacketEvents failed to find the temporary data file.");
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(
                    ChatColor.RED + "PacketEvents failed to read from the temporary data file.");
        }
        return data;
    }

    private static void writeToTempDataFile(String data, File tempDataFile) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempDataFile))) {
            bufferedWriter.write(data);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(
                    ChatColor.RED + "PacketEvents failed to write to the temporary data file.");
        }
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        if (!VersionLookupUtils.hasLoaded()) {
            VersionLookupUtils.load();
        }
        ClientVersion version = ClientVersion.fromProtocolVersion(VersionLookupUtils.getProtocolVersion(e.getPlayer()));
        PacketEvents.getAPI().getPlayerUtils().clientVersionsMap.put(e.getPlayer().getUniqueId(), version);
        PacketEvents.getAPI().getPlayerUtils().injectPlayer(e.getPlayer());
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        PacketEvents.getAPI().getPlayerUtils().ejectPlayer(e.getPlayer());
        PacketEvents.getAPI().getPlayerUtils().clientVersionsMap.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        BukkitMoveEvent moveEvent = new BukkitMoveEvent(e);
        PacketEvents.getAPI().getEventManager().callEvent(moveEvent);
        e.setCancelled(moveEvent.isCancelled());
    }

}