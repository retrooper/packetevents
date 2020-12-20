/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.packetwrappers.play.out.abilities;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

final class PlayerAbilitiesUtils {
    public static Class<?> playerAbilitiesClass;
    public static Constructor<?> playerAbilitiesConstructor;

    public static Object getPlayerAbilities(final boolean isVulnerable, final boolean isFlying, final boolean allowFlight, final boolean canBuildInstantly,
                                            final float flySpeed, final float walkSpeed) throws IllegalAccessException, InvocationTargetException, InstantiationException {

        if(playerAbilitiesClass == null) {
            try {
                playerAbilitiesClass = NMSUtils.getNMSClass("PlayerAbilities");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if(playerAbilitiesConstructor == null && playerAbilitiesClass != null) {
            try {
                playerAbilitiesConstructor = playerAbilitiesClass.getConstructor();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        final Object instance = Objects.requireNonNull(playerAbilitiesConstructor).newInstance();
        WrappedPacket wrapper = new WrappedPacket(new NMSPacket(instance));
        wrapper.writeBoolean(0, isVulnerable);
        wrapper.writeBoolean(1, isFlying);
        wrapper.writeBoolean(2, allowFlight);
        wrapper.writeBoolean(3, canBuildInstantly);
        wrapper.writeFloat(0, flySpeed);
        wrapper.writeFloat(1, walkSpeed);
        return instance;
    }
}
