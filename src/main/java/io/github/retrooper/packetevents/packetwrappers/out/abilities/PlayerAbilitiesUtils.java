package io.github.retrooper.packetevents.packetwrappers.out.abilities;

import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

final class PlayerAbilitiesUtils {
    public static Class<?> playerAbilitiesClass;
    public static Constructor<?> playerAbilitiesConstructor;

    static {
        try {
            playerAbilitiesClass = NMSUtils.getNMSClass("PlayerAbilities");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            playerAbilitiesConstructor = playerAbilitiesClass.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static Object getPlayerAbilities(final boolean isVulnerable, final boolean isFlying, final boolean allowFlight, final boolean canBuildInstantly,
                                            final float flySpeed, final float walkSpeed) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        final Object instance = playerAbilitiesConstructor.newInstance();
        Reflection.getField(playerAbilitiesClass, boolean.class, 0).setBoolean(instance, isVulnerable);
        Reflection.getField(playerAbilitiesClass, boolean.class, 1).setBoolean(instance, isFlying);
        Reflection.getField(playerAbilitiesClass, boolean.class, 2).setBoolean(instance, allowFlight);
        Reflection.getField(playerAbilitiesClass, boolean.class, 3).setBoolean(instance, canBuildInstantly);

        Reflection.getField(playerAbilitiesClass, float.class, 0).setFloat(instance, flySpeed);
        Reflection.getField(playerAbilitiesClass, float.class, 1).setFloat(instance, walkSpeed);
        return instance;
    }
}
