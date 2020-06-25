package io.github.retrooper.packetevents.packetwrappers.out.abilities;

import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

final class PlayerAbilitiesUtils {

    public static Class<?> playerAbilitiesClass;

    public static Constructor<?> playerAbilitiesConstructor;

    private static final Reflection.FieldAccessor<Boolean>[] booleanFieldAccessors = new Reflection.FieldAccessor[4];
    private static final Reflection.FieldAccessor<Float>[] floatFieldAccessors = new Reflection.FieldAccessor[2];

    public static Object getPlayerAbilities(final boolean isVulnerable, final boolean isFlying, final boolean allowFlight, final boolean canBuildInstantly,
                                            final float flySpeed, final float walkSpeed) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        final Object instance = playerAbilitiesConstructor.newInstance();
        booleanFieldAccessors[0].set(instance, isVulnerable);
        booleanFieldAccessors[1].set(instance, isFlying);
        booleanFieldAccessors[2].set(instance, allowFlight);
        booleanFieldAccessors[3].set(instance, canBuildInstantly);

        floatFieldAccessors[0].set(instance, flySpeed);
        floatFieldAccessors[1].set(instance, walkSpeed);

        return instance;
    }

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

        for (byte i = 0; i < booleanFieldAccessors.length; i++) {
            booleanFieldAccessors[i] = Reflection.getField(playerAbilitiesClass, boolean.class, i);
        }

        for (byte i = 0; i < floatFieldAccessors.length; i++) {
            floatFieldAccessors[i] = Reflection.getField(playerAbilitiesClass, float.class, i);
        }
    }
}
