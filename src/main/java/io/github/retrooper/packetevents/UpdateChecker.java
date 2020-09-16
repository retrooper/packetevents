package io.github.retrooper.packetevents;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UpdateChecker extends BukkitRunnable {

    private final Plugin plugin;
    private String newVersion;

    public UpdateChecker(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        try {
            newVersion = readLines();

            //Please don't compare two strings using ==... ILL FIND WHERE U LIVE
            if (PacketEvents.getVersion().equals(newVersion)) {
                //We don't wanna access the Bukkit API Async (It really does not make a difference but Just to be safe)
                inform();
            }
        } catch (IOException ignored) {
            //Thats fine, Not important
        }
    }

    private void inform() {
        new BukkitRunnable() {
            public void run() {
                Bukkit.getLogger().info("[PacketEvents] There is an update available for PacketEventsAPI! (" + newVersion + ")");
            }
        }.runTask(plugin);
    }

    private String readLines() throws IOException {
        URLConnection connection = new URL("https://api.spigotmc.org/legacy/update.php?resource=80279").openConnection();
        connection.addRequestProperty("User-Agent", "Mozilla/4.0");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        return reader.readLine();
    }
}