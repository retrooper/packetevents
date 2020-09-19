package io.github.retrooper.packetevents;

import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UpdateChecker implements Runnable {
    @Override
    public void run() {
        try {

            PEVersion newVersion = new PEVersion(readLines());
            PEVersion localVersion = PacketEvents.getVersion();
            if (localVersion.isOlder(newVersion)) {
                inform("[PacketEvents] There is an update available for the PacketEvents API! (" + newVersion.toString() + ")");
            }
            else if(localVersion.equals(newVersion)) {
                inform("[PacketEvents] You are on the latest released version of PacketEvents. (" + newVersion.toString() + ")");
            }
            else if(localVersion.isNewer(newVersion)) {
                inform("[PacketEvents] You are on a dev or pre released build of PacketEvents. Your build: (" + localVersion.toString() + ") | Last released build: (" + newVersion.toString() + ")");
            }
        } catch (IOException ignored) {
            //Thats fine, Not important
        }
    }

    private void inform(String message) {
        Bukkit.getLogger().info(message);
    }

    private String readLines() throws IOException {
        URLConnection connection = new URL("https://api.spigotmc.org/legacy/update.php?resource=80279").openConnection();
        connection.addRequestProperty("User-Agent", "Mozilla/4.0");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        return reader.readLine();
    }
}