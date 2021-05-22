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

package io.github.retrooper.packetevents.utils.npc;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
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
