package io.github.retrooper.packetevents.packetwrappers.play.in.difficultychange;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;

public final class WrappedPacketInDifficultyChange extends WrappedPacket {

    private static Class<? extends Enum<?>> enumDifficultyClass;

    public WrappedPacketInDifficultyChange(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        try {
            enumDifficultyClass = (Class<? extends Enum<?>>) NMSUtils.getNMSClass("EnumDifficulty");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Difficulty getDifficulty() {
        Enum<?> enumConstant = (Enum<?>) readObject(0, enumDifficultyClass);
        return Difficulty.values()[enumConstant.ordinal()] ;
    }

    public void setDifficulty(Difficulty difficulty) {
        Enum<?> enumConstant = EnumUtil.valueByIndex(enumDifficultyClass, difficulty.ordinal());
        write(enumDifficultyClass, 0, enumConstant);
    }

    public enum Difficulty {
        PEACEFUL,
        EASY,
        NORMAL,
        HARD
    }
}
