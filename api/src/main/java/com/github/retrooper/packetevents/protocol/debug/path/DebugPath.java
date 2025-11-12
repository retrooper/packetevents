/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.debug.path;

import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Set;

/**
 * Network structure for representing vanilla pathfinder paths.
 *
 * @versions 1.21.9+
 */
@NullMarked
public final class DebugPath {

    private final boolean reached;
    private final int nextNodeIndex;
    private final Vector3i target;
    private final List<DebugNode> nodes;
    private final Set<DebugNode> targetNodes;
    private final List<DebugNode> openSet;
    private final List<DebugNode> closedSet;

    public DebugPath(
            boolean reached, int nextNodeIndex, Vector3i target, List<DebugNode> nodes,
            Set<DebugNode> targetNodes, List<DebugNode> openSet, List<DebugNode> closedSet
    ) {
        this.reached = reached;
        this.nextNodeIndex = nextNodeIndex;
        this.target = target;
        this.nodes = nodes;
        this.targetNodes = targetNodes;
        this.openSet = openSet;
        this.closedSet = closedSet;
    }

    public static DebugPath read(PacketWrapper<?> wrapper) {
        boolean reached = wrapper.readBoolean();
        int nextNodeIndex = wrapper.readInt();
        Vector3i target = wrapper.readBlockPosition();
        List<DebugNode> nodes = wrapper.readList(DebugNode::read);
        Set<DebugNode> targetNodes = wrapper.readSet(DebugNode::read);
        List<DebugNode> openSet = wrapper.readList(DebugNode::read);
        List<DebugNode> closedSet = wrapper.readList(DebugNode::read);
        return new DebugPath(reached, nextNodeIndex, target, nodes, targetNodes, openSet, closedSet);
    }

    public static void write(PacketWrapper<?> wrapper, DebugPath path) {
        wrapper.writeBoolean(path.reached);
        wrapper.writeInt(path.nextNodeIndex);
        wrapper.writeBlockPosition(path.target);
        wrapper.writeList(path.nodes, DebugNode::write);
        wrapper.writeSet(path.targetNodes, DebugNode::write);
        wrapper.writeList(path.openSet, DebugNode::write);
        wrapper.writeList(path.closedSet, DebugNode::write);
    }

    public boolean isReached() {
        return this.reached;
    }

    public int getNextNodeIndex() {
        return this.nextNodeIndex;
    }

    public Vector3i getTarget() {
        return this.target;
    }

    public List<DebugNode> getNodes() {
        return this.nodes;
    }

    public Set<DebugNode> getTargetNodes() {
        return this.targetNodes;
    }

    public List<DebugNode> getOpenSet() {
        return this.openSet;
    }

    public List<DebugNode> getClosedSet() {
        return this.closedSet;
    }
}
