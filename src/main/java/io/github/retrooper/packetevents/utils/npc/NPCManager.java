package io.github.retrooper.packetevents.utils.npc;

import io.github.retrooper.packetevents.utils.list.ConcurrentList;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NPCManager {
    private final Map<Integer, NPC> npcMap = new ConcurrentHashMap<>();
    private final List<NPC> npcList = new ConcurrentList<>();

    @Nullable
    public NPC getNPCById(int entityID) {
        return npcMap.get(entityID);
    }

    public List<NPC> getNPCList() {
        return npcList;
    }

    public void registerNPC(NPC npc) {
        npcMap.put(npc.getEntityId(), npc);
        npcList.add(npc);
    }

    public void unregisterNPC(NPC npc) {
        npcMap.remove(npc.getEntityId());
        npcList.remove(npc);
    }
}
