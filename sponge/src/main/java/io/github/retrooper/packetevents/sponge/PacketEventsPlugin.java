/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package io.github.retrooper.packetevents.sponge;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.TimeStampMode;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowItems;
import com.google.inject.Inject;
import io.github.retrooper.packetevents.sponge.factory.SpongePacketEventsBuilder;
import io.github.retrooper.packetevents.sponge.internal.AdventureInfo;
import io.github.retrooper.packetevents.sponge.util.SpongeConversionUtil;
import net.kyori.adventure.Adventure;
import org.bstats.charts.SimplePie;
import org.bstats.sponge.Metrics;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.Objects;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

@Plugin("packetevents")
public class PacketEventsPlugin {

    private final PluginContainer pluginContainer;
    private final Metrics metrics;

    @Inject
    public PacketEventsPlugin(PluginContainer pluginContainer, Metrics.Factory metricsFactory) {
        this.pluginContainer = pluginContainer;
        this.metrics = metricsFactory.make(11327);
    }

    @Listener(order = Order.EARLY)
    public void onServerStart(final StartingEngineEvent<Server> event) {
        PacketEvents.setAPI(SpongePacketEventsBuilder.build(pluginContainer));
        PacketEvents.getAPI().load();

        // Register your listeners
        PacketEvents.getAPI().getSettings().debug(false).downsampleColors(false).checkForUpdates(true).timeStampMode(TimeStampMode.MILLIS).reEncodeByDefault(true);
        PacketEvents.getAPI().init();

        if (!this.validateAdventureVersion()) {
            PacketEvents.getAPI().getLogManager().warn("Adventure version mismatch detected! This may cause issues with PacketEvents.");
            PacketEvents.getAPI().getLogManager().warn("Ensure that you are using the version of PacketEvents intended for this version of Sponge.");
            PacketEvents.getAPI().getLogManager().warn("This version of PacketEvents was built for Sponge API: " + AdventureInfo.EXPECTED_SPONGE_VERSION);
            PacketEvents.getAPI().getLogManager().warn("It is possible that Sponge has not yet updated to the correct Adventure API version for this Minecraft version.");
            PacketEvents.getAPI().getLogManager().warn("If this is the case, please kindly ask the Sponge team to update their Adventure API to the correct version.");
        }

        //Just to have an idea of which versions of packetevents people use
        metrics.addCustomChart(new SimplePie("packetevents_version", () -> PacketEvents.getAPI().getVersion().toStringWithoutSnapshot()));

        SimplePacketListenerAbstract listener = new SimplePacketListenerAbstract(PacketListenerPriority.HIGH) {

            // Testing ItemStack conversion, can be removed in future
            @Override
            public void onPacketPlaySend(PacketPlaySendEvent event) {
                if (event.getPacketType() == PacketType.Play.Server.WINDOW_ITEMS) {
                    WrapperPlayServerWindowItems items = new WrapperPlayServerWindowItems(event);
                    for (ItemStack item : items.getItems()) {
                        org.spongepowered.api.item.inventory.ItemStack sponge = SpongeConversionUtil.toSpongeItemStack(item);
                        System.out.println(sponge.type().key(RegistryTypes.ITEM_TYPE).formatted());
                        System.out.println(SpongeConversionUtil.fromSpongeItemStack(sponge).getType().getName().toString());
                    }
                }
            }

            @Override
            public void onUserConnect(UserConnectEvent event) {
                PacketEvents.getAPI().getLogManager().debug("User: (host-name) " + event.getUser().getAddress().getHostString() + " connected...");
            }

            @Override
            public void onUserLogin(UserLoginEvent event) {
                PacketEvents.getAPI().getLogManager().debug("You logged in! User name: " + event.getUser().getProfile().getName());
            }

            @Override
            public void onUserDisconnect(UserDisconnectEvent event) {
                PacketEvents.getAPI().getLogManager().debug("User: (host-name) " + event.getUser().getAddress().getHostString() + " disconnected...");
            }
        };
//        PacketEvents.getAPI().getEventManager().registerListener(listener);
    }

    @Listener(order = Order.LATE)
    public void onStopping(StoppingEngineEvent<Server> event) {
        PacketEvents.getAPI().terminate();
    }

    /**
     * As we do not shade adventure for Sponge, we need to validate Sponge bundles the version we expect.
     * A mismatched version indicates any of the following:
     * a) Sponge has not yet updated Adventure to the correct version for the Minecraft version they are on.
     * b) PacketEvents has not yet updated Adventure, and Sponge is newer.
     * c) This version of PacketEvents was not built for this version of Sponge. We expect to target the latest Sponge API.
     */
    private boolean validateAdventureVersion() {
        String bundledAdventureVersion = null;

        try {
            // Sponge loads from libraries folder.
            CodeSource src = Adventure.class.getProtectionDomain().getCodeSource();
            if (src == null) {
                PacketEvents.getAPI().getLogManager().warn("Unable to resolve CodeSource for Adventure JAR");
                return false;
            }

            String urlStr = src.getLocation().toString(); // e.g., "jar:file:///C:/.../adventure-api-4.24.0.jar!/"

            // Strip jar: prefix and !/ suffix
            if (urlStr.startsWith("jar:")) urlStr = urlStr.substring(4);
            if (urlStr.endsWith("!/")) urlStr = urlStr.substring(0, urlStr.length() - 2);

            // Convert URL -> URI -> File
            URL fileUrl = new URL(urlStr);
            URI uri = fileUrl.toURI();
            File jarFile = new File(uri);

            if (!jarFile.exists()) {
                PacketEvents.getAPI().getLogManager().warn("Adventure JAR file not found: " + jarFile);
                return false;
            }

            // Open jar and read manifest
            try (JarFile jar = new JarFile(jarFile)) {
                Manifest mf = jar.getManifest();
                if (mf != null) {
                    bundledAdventureVersion = mf.getMainAttributes().getValue("Specification-Version");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        PacketEvents.getAPI().getLogManager().info("Runtime Adventure Version: " + bundledAdventureVersion);

        if (bundledAdventureVersion == null) {
            PacketEvents.getAPI().getLogManager().warn("Failed to validate Adventure version!");
            return false;
        }

        final String expectedAdventureVersion = AdventureInfo.EXPECTED_ADVENTURE_VERSION;
        PacketEvents.getAPI().getLogManager().info("Expected Adventure Version: " + expectedAdventureVersion);

        return Objects.equals(bundledAdventureVersion, expectedAdventureVersion);
    }
}
