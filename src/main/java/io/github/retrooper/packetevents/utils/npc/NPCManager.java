package io.github.retrooper.packetevents.utils.npc;

import io.github.retrooper.packetevents.utils.list.ConcurrentList;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NPCManager {
    private final Map<Integer, NPC> npcMap = new ConcurrentHashMap<>();

    @Nullable
    public NPC getNPCById(int entityID) {
        return npcMap.get(entityID);
    }

    public Collection<NPC> getNPCList() {
        return npcMap.values();
    }

    public void registerNPC(NPC npc) {
        npcMap.put(npc.getEntityId(), npc);
    }

    public void unregisterNPC(NPC npc) {
        npcMap.remove(npc.getEntityId());
    }
}
