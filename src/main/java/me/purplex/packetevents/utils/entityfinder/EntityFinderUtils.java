package me.purplex.packetevents.utils.entityfinder;

import me.purplex.packetevents.PacketEvents;
import me.purplex.packetevents.enums.ServerVersion;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class EntityFinderUtils {
    private static ServerVersion version = PacketEvents.getServerVersion();

    public static Entity getEntityById(final World w, final int id) {
        if (w == null) return null;
        if(version == ServerVersion.v_1_7_10) {
            return EntityFinder_1_7_10.getEntityById(w, id);
        }
        else if(version == ServerVersion.v_1_8) {
            return EntityFinder_1_8.getEntityById(w, id);
        }
        else if(version == ServerVersion.v_1_8_3) {
            return EntityFinder_1_8_3.getEntityById(w, id);
        }
        else if(version==ServerVersion.v_1_8_8) {
            return EntityFinder_1_8_8.getEntityById(w, id);
        }
        else if(version==ServerVersion.v_1_9) {
            return EntityFinder_1_9.getEntityById(w, id);
        }
        else if(version == ServerVersion.v_1_10) {
            return EntityFinder_1_10.getEntityById(w, id);
        }
        else if(version == ServerVersion.v_1_11) {
            return EntityFinder_1_11.getEntityById(w, id);
        }
        else if(version == ServerVersion.v_1_12) {
            return EntityFinder_1_12.getEntityById(w, id);
        }
        else if(version == ServerVersion.v_1_13) {
            return EntityFinder_1_13.getEntityById(w, id);
        }
        else if(version == ServerVersion.v_1_13_2) {
            return EntityFinder_1_13_2.getEntityById(w, id);
        }
        else if(version == ServerVersion.v_1_14) {
            return EntityFinder_1_14.getEntityById(w, id);
        }
        else if(version == ServerVersion.v_1_15) {
            return EntityFinder_1_15.getEntityById(w, id);
        }
        return null;
    }
}
