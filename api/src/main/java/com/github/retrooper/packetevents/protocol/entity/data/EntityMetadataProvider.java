package com.github.retrooper.packetevents.protocol.entity.data;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;

import java.util.List;

/**
 * This is an interface that provides a list of entity data.
 * This allows custom addon libraries to implement metadata classes for various entities
 * which can then be used in our API.
 * @since 2.1.0
 */
public interface EntityMetadataProvider {
    /**
     * PacketEvents is retrieving the metadata of this specific entity.
     * PacketEvents provides you with the protocol version that the library is running on
     * to allow you tu specify different metadata depending on the version.
     * You are free to ignore it at the cost of your features not working on all versions.
     * @param version The protocol version of the format we expect the metadata to be in.
     * @return Metadata (list of entity data)
     */
    List<EntityData> entityData(ClientVersion version);

    /**
     * Similar to {@link #entityData(ClientVersion)} but does not provide a protocol version.
     * It is not advised to use nor implement this.
     * It is deprecated because it restricts you by not providing you the format in which the metadata should be.
     * @return Metadata (list of entity data)
     * @deprecated Does not specify format of metadata, therefore not advised to use this. It will assume the current latest version.
     */
    @Deprecated
    default List<EntityData> entityData() {
        return entityData(ClientVersion.getLatest());
    }
}
