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

package io.github.retrooper.packetevents.util.folia;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

/**
 * Utility class to handle scheduling tasks.
 * It uses Paper's threaded-regions schedulers if Folia is used,
 * otherwise it falls back to the default Bukkit scheduler.
 */
public class FoliaScheduler {
    static final boolean isFolia;
    private static Class<? extends Event> regionizedServerInitEventClass;

    private static AsyncScheduler asyncScheduler;
    private static EntityScheduler entityScheduler;
    private static GlobalRegionScheduler globalRegionScheduler;
    private static RegionScheduler regionScheduler;

    static {
        boolean folia;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            folia = true;

            // Thanks for this code ViaVersion
            // The class is only part of the Folia API, so we need to use reflections to get it
            regionizedServerInitEventClass = (Class<? extends Event>) Class.forName("io.papermc.paper.threadedregions.RegionizedServerInitEvent");
        } catch (ClassNotFoundException e) {
            folia = false;
        }

        isFolia = folia;
    }

    /**
     * @return Whether the server is running Folia
     */
    public static boolean isFolia() {
        return isFolia;
    }

    /**
     * Returns the async scheduler.
     *
     * @return async scheduler instance of {@link AsyncScheduler}
     */
    public static AsyncScheduler getAsyncScheduler() {
        if (asyncScheduler == null) {
            asyncScheduler = new AsyncScheduler();
        }
        return asyncScheduler;
    }

    /**
     * Returns the entity scheduler.
     *
     * @return entity scheduler instance of {@link EntityScheduler}
     */
    public static EntityScheduler getEntityScheduler() {
        if (entityScheduler == null) {
            entityScheduler = new EntityScheduler();
        }
        return entityScheduler;
    }

    /**
     * Returns the global region scheduler.
     *
     * @return global region scheduler instance of {@link GlobalRegionScheduler}
     */
    public static GlobalRegionScheduler getGlobalRegionScheduler() {
        if (globalRegionScheduler == null) {
            globalRegionScheduler = new GlobalRegionScheduler();
        }
        return globalRegionScheduler;
    }

    /**
     * Returns the region scheduler.
     *
     * @return region scheduler instance of {@link RegionScheduler}
     */
    public static RegionScheduler getRegionScheduler() {
        if (regionScheduler == null) {
            regionScheduler = new RegionScheduler();
        }
        return regionScheduler;
    }

    /**
     * Run a task after the server has finished initializing.
     * Undefined behavior if called after the server has finished initializing.
     * <p>
     * We still need to use reflections to get the server init event class, as this is only part of the Folia API.
     *
     * @param plugin Your plugin or PacketEvents
     * @param run    The task to run
     */
    public static void runTaskOnInit(Plugin plugin, Runnable run) {
        if (!isFolia) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, run);
            return;
        }

        Bukkit.getServer().getPluginManager().registerEvent(regionizedServerInitEventClass, new Listener() {
        }, EventPriority.HIGHEST, (listener, event) -> run.run(), plugin);
    }
}