package com.github.retrooper.packetevents.test;

import com.github.retrooper.packetevents.annotations.RuntimeObsolete;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import com.github.retrooper.packetevents.test.base.BaseDummyAPITest;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.github.retrooper.packetevents.PacketEvents;
import org.bukkit.Material;
import org.bukkit.material.MaterialData; //Needed for 1.12 and below support.
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class StateTypeMappingTest extends BaseDummyAPITest {

    private Collection<StateType> cachedStateValues = null;

    private Collection<StateType> getNonObsoleteStateTypes() {
        if (cachedStateValues == null) {
            cachedStateValues = computeNonObsoleteStateTypes();
        }
        return cachedStateValues;
    }

    private Collection<StateType> computeNonObsoleteStateTypes() {
        // Case 0: Ignore obsolete entries
        return StateTypes.values().stream().filter(e -> {
            try {
                return StateTypes.class.getField(e.getName().toUpperCase(Locale.ROOT)).getAnnotation(RuntimeObsolete.class) == null;
            } catch (NoSuchFieldException ex) {
                throw new RuntimeException(ex);
            }
        }).collect(Collectors.toList());
    }

    @ParameterizedTest
    @EnumSource(ClientVersion.class)
    @DisplayName("Verify StateType mappings for all client versions")
    public void testStateTypeMappings(ClientVersion version) {
        testStateTypeMappings(version, false);
    }

    @Test
    @DisplayName("Verify StateType mappings (fail fast)")
    public void testStateTypeMappingsFailFast() {
        // Use the server version.
        ServerVersion serverVersion = PacketEvents.getAPI().getServerManager().getVersion();
        ClientVersion version = serverVersion.toClientVersion();
        testStateTypeMappings(version, true);
    }

    public void testStateTypeMappings(ClientVersion version, boolean failFast) {
        final ServerVersion serverVersion = PacketEvents.getAPI().getServerManager().getVersion();
        Function<Material, WrappedBlockState> blockStateFunction = getBlockStateFunction(serverVersion);

        StringBuilder errorMessages = new StringBuilder(); // Accumulate error messages for diagnostic mode

        int found = 0;
        int idsMatched = 0;


        Collection<StateType> stateValues = getNonObsoleteStateTypes();

        // Get all BlockTags for versions newer than the server version
        List<BlockTags> newerBlockTags = getVersionBlockTagsNewerThan(serverVersion);
        int expected = stateValues.size();

        // Check if the client version is newer than the server version
        ClientVersion serverClientVersion = serverVersion.toClientVersion();
        boolean isClientNewer = version.isNewerThan(serverClientVersion);

        for (StateType value : stateValues) {
            String name = value.getName();
            int id = value.getMapped().getId(version);

            // Case 1: Block is from a newer version than the server (id == -1)
            if (id == -1 && isBlockFromNewerVersion(value, newerBlockTags)) {
                // Skip validation for blocks from newer versions and mark as found so there is no count error at the end
                expected--;
                continue;
            }

            Material material = Material.matchMaterial(name); // This will return null for materials like potted_open_eyeblossom (added in 1.21.4) on 1.21 server

            // Case 2: Client is newer, block exists in client (id != -1), but not in server (material == null)
            if (isClientNewer && id != -1 && material == null && isBlockFromNewerVersion(value, newerBlockTags)) {
                // This is expected behavior: the client knows the block, but the server does not
                expected--;
                continue;
            }

            // Case 3: Material is missing unexpectedly (not from a newer version)
            if (material == null) {
                String errorMessage = String.format("Material not found for statetype %s, id=%d", name, id);
                if (failFast) {
                    fail(errorMessage);
                    return; // Just to make sure it exits.
                } else {
                    errorMessages.append(errorMessage).append("\n");
                }
                continue;
            }
            found++;

            WrappedBlockState state = blockStateFunction.apply(material);
            if (state == null) {
                String errorMessage = String.format("Failed to create BlockState from material %s, id=%d", material.name(), id);
                if (failFast) {
                    fail(errorMessage);
                    return;
                } else {
                    errorMessages.append(errorMessage).append("\n");
                }
                continue;
            }

            if (state.getType() != value) {
                String errorMessage = String.format("State type mismatch for material %s, type=%s, value=%s", material.name(), state.getType(), value);
                if (failFast) {
                    fail(errorMessage);
                    return;
                } else {
                    errorMessages.append(errorMessage).append("\n");
                }
                continue;
            }
            idsMatched++;
        }

        final int missing = expected - found;

        // Diagnostic output (non-fail-fast mode)
        if (!failFast && errorMessages.length() > 0) {
            System.err.println("StateType Mapping Errors:");
            System.err.println(errorMessages);

            // Output summary
            System.err.println(String.format("%d/%d statetypes found", found, expected));
            if (missing > 0) {
                double percent = ((double) found / expected) * 100;
                System.err.println(String.format("%d missing (%.2f%%)", missing, percent));
            }
            System.err.println(String.format("%d/%d ids matched", idsMatched, found));
        }

        // Only fail the test if there are unexpected missing StateTypes
        assertEquals(expected, found, String.format("Not all StateTypes found for version %s. Missing: %d. See error log for details.", version.getReleaseName(), missing));
        assertEquals(found, idsMatched, String.format("Not all StateType IDs matched for version %s. See error log for details.", version.getReleaseName()));
    }

    private Function<Material, WrappedBlockState> getBlockStateFunction(ServerVersion serverVersion) {
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_12)) {
            return material -> SpigotConversionUtil.fromBukkitMaterialData(new MaterialData(material));
        } else {
            return material -> SpigotConversionUtil.fromBukkitBlockData(material.createBlockData());
        }
    }

    /**
     * Gets all BlockTags for versions newer than the server version.
     * Relies on BlockTags existing with names V_1_20_5, V_1_21_2, V_1_21_4, etc...
     * for versions newer than the Mocked server version (currently 1.21.1 from MockBukkit)
     */
    private List<BlockTags> getVersionBlockTagsNewerThan(ServerVersion serverVersion) {
        List<BlockTags> blockTags = new ArrayList<>();
        for (ServerVersion version : ServerVersion.values()) {
            if (version.isNewerThan(serverVersion)) { // Use isNewerThan to exclude the server's own version
                BlockTags blockTag = BlockTags.getByName(version.name().toLowerCase(Locale.ROOT)); // Use name() to match enum naming convention
                if (blockTag != null) { // Only add non-null tags
                    blockTags.add(blockTag);
                }
            }
        }
        return blockTags;
    }

    /**
     * Determines if the block is from a version newer than the server's version.
     */
    private boolean isBlockFromNewerVersion(StateType stateType, List<BlockTags> newerBlockTags) {
        // Check if the StateType is tagged in any of the newer BlockTags
        for (BlockTags tag : newerBlockTags) {
            if (tag.contains(stateType)) {
                return true;
            }
        }
        return false;
    }
}