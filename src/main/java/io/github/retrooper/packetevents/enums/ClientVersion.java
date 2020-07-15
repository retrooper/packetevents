package io.github.retrooper.packetevents.enums;

public enum ClientVersion {
    LESS_THAN_V_1_7_10, v_1_7_10, v_1_8, v_1_9, v_1_9_1, v_1_9_2, v_1_9_3, v_1_10, v_1_11, v_1_11_1, v_1_12, v_1_12_1, v_1_12_2,
    v_1_13, v_1_13_1, v_1_13_2, v_1_14, v_1_14_1, v_1_14_2, v_1_14_3, v_1_14_4, v_1_15, v_1_15_1, v_1_15_2, v_1_16, v_1_16_1, HIGHER_THAN_V_1_16_1, INVALID, ACCESS_FAILURE;

    private static final Map<Integer, ClientVersion> VERSION_LOOKUP = new HashMap<>();

    public static void prepareLookUp() {
        VERSION_LOOKUP.put(-1, ACCESS_FAILURE);
        VERSION_LOOKUP.put(1, LESS_THAN_V_1_7_10);
        VERSION_LOOKUP.put(2, LESS_THAN_V_1_7_10);
        VERSION_LOOKUP.put(3, LESS_THAN_V_1_7_10);
        VERSION_LOOKUP.put(4, LESS_THAN_V_1_7_10);
        VERSION_LOOKUP.put(5, v_1_7_10);
        VERSION_LOOKUP.put(47, v_1_8);
        VERSION_LOOKUP.put(107, v_1_9);
        VERSION_LOOKUP.put(108, v_1_9_1);
        VERSION_LOOKUP.put(109, v_1_9_2);
        VERSION_LOOKUP.put(110, v_1_9_3);
        VERSION_LOOKUP.put(210, v_1_10);
        VERSION_LOOKUP.put(315, v_1_11);
        VERSION_LOOKUP.put(316, v_1_11_1);
        VERSION_LOOKUP.put(335, v_1_12);
        VERSION_LOOKUP.put(338, v_1_12_1);
        VERSION_LOOKUP.put(340, v_1_12_2);
        VERSION_LOOKUP.put(393, v_1_13);
        VERSION_LOOKUP.put(401, v_1_13_1);
        VERSION_LOOKUP.put(404, v_1_13_2);
        VERSION_LOOKUP.put(477, v_1_14);
        VERSION_LOOKUP.put(480, v_1_14_1);
        VERSION_LOOKUP.put(485, v_1_14_2);
        VERSION_LOOKUP.put(490, v_1_14_3);
        VERSION_LOOKUP.put(498, v_1_14_4);
        VERSION_LOOKUP.put(573, v_1_15);
        VERSION_LOOKUP.put(575, v_1_15_1);
        VERSION_LOOKUP.put(578, v_1_15_2);
        VERSION_LOOKUP.put(735, v_1_16);
        VERSION_LOOKUP.put(736, v_1_16_1);
    }
    
    public static ClientVersion fromProtocolVersion(int protocolVersion) {
        if(protocolVersion == -1 && PacketEvents.getSettings().doAutoResolveClientProtocolVersion()) {
           protocolVersion = ServerVersion.getVersion().toProtocolVersion();
        }
        final ClientVersion lookedUp = VERSION_LOOKUP.get(protocolVersion);
        if(lookedUp != null) {
            return lookedUp;
        }
        return protocolVersion > 736 ? HIGHER_THAN_V_1_16_1 : INVALID;
    }

    /**
     * Returns if the client's version is more up to date than the argument passed version
     *
     * @param version
     * @return
     */
    public boolean isHigherThan(final ClientVersion version) {
        if (this == version) return false;
        return !isLowerThan(version);
    }

    /**
     * Returns if the client's version is more outdated than the argument passed version
     *
     * @param version
     * @return
     */
    public boolean isLowerThan(final ClientVersion version) {
        if (version == this) return false;
        final int len = values().length;
        for (byte i = 0; i < len; i++) {
            final ClientVersion v = values()[i];
            if (v == this) {
                return true;
            } else if (v == version) {
                return false;
            }
        }
        return false;
    }
}
