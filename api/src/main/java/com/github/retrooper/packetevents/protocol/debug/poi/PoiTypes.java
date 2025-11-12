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

package com.github.retrooper.packetevents.protocol.debug.poi;

import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PoiTypes {

    private static final VersionedRegistry<PoiType> REGISTRY = new VersionedRegistry<>("point_of_interest_type");

    private PoiTypes() {
    }

    public static VersionedRegistry<PoiType> getRegistry() {
        return REGISTRY;
    }

    @ApiStatus.Internal
    public static PoiType define(String name) {
        return REGISTRY.define(name, StaticPoiType::new);
    }

    public static final PoiType ARMORER = define("armorer");
    public static final PoiType BUTCHER = define("butcher");
    public static final PoiType CARTOGRAPHER = define("cartographer");
    public static final PoiType CLERIC = define("cleric");
    public static final PoiType FARMER = define("farmer");
    public static final PoiType FISHERMAN = define("fisherman");
    public static final PoiType FLETCHER = define("fletcher");
    public static final PoiType LEATHERWORKER = define("leatherworker");
    public static final PoiType LIBRARIAN = define("librarian");
    public static final PoiType MASON = define("mason");
    public static final PoiType SHEPHERD = define("shepherd");
    public static final PoiType TOOLSMITH = define("toolsmith");
    public static final PoiType WEAPONSMITH = define("weaponsmith");
    public static final PoiType HOME = define("home");
    public static final PoiType MEETING = define("meeting");
    public static final PoiType BEEHIVE = define("beehive");
    public static final PoiType BEE_NEST = define("bee_nest");
    public static final PoiType NETHER_PORTAL = define("nether_portal");
    public static final PoiType LODESTONE = define("lodestone");
    public static final PoiType TEST_INSTANCE = define("test_instance");
    public static final PoiType LIGHTNING_ROD = define("lightning_rod");

    static {
        REGISTRY.unloadMappings();
    }
}
