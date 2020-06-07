package io.github.retrooper.packetevents.enums;

public enum ClientVersion {
    LESS_THAN_V_1_7_10, v_1_7_10, v_1_8, v_1_9, v_1_9_1, v_1_9_2, v_1_9_3, v_1_10, v_1_11, v_1_11_1, v_1_12, v_1_12_1, v_1_12_2,
    v_1_13, v_1_13_1, v_1_13_2, v_1_14, v_1_14_1, v_1_14_2, v_1_14_3, v_1_14_4, v_1_15, v_1_15_1, v_1_15_2, HIGHER_THAN_V_1_15_2, INVALID;

    public static ClientVersion fromProtocolVersion(final int protocolVersion) {
        int remainder = -1;
        switch (protocolVersion) {
            case 1:
            case 2:
            case 3:
            case 4:
                return LESS_THAN_V_1_7_10;
            case 47://ranging from 1.8->1.8.9
                return v_1_8;
            case 107:
                return v_1_9;
            case 108:
                return v_1_9_1;
            case 109:
                return v_1_9_2;
            case 110:
                return v_1_9_3; //can be 1.9.3 or 1.9.4
            case 210:
                return v_1_10; //could be 1.10,1.10.1, 1.10.2
            case 315:
                return v_1_11;
            case 316: //could be 11.1 or 11.2
                return v_1_11_1;
            case 335:
                return v_1_12;
            case 338:
                return v_1_12_1;
            case 340:
                return v_1_12_2;
            case 393:
                return v_1_13;
            case 401:
                return v_1_13_1;
            case 404:
                return v_1_13_2;
            case 477:
                return v_1_14;
            case 480:
                return v_1_14_1;
            case 485:
                return v_1_14_2;
            case 490:
                return v_1_14_3;
            case 498:
                return v_1_14_4;
            case 573:
                return v_1_15;
            case 575:
                return v_1_15_1;
            case 578:
                return v_1_15_2;
            default:
                remainder = protocolVersion;
        }
        if(remainder > 578) {
            return HIGHER_THAN_V_1_15_2;
        }
        return INVALID;
    }
}
