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

package io.github.retrooper.packetevents.utils.gameprofile;

import com.mojang.authlib.GameProfile;

import java.util.UUID;

/**
 * 1.8 (and above) Mojang Game Profile util using the 1.8 (and above) Mojang API import location.
 *
 * @author retrooper
 * @since 1.6.8.2
 */
class GameProfileUtil_8 {
    /**
     * Create a new Mojang Game Profile object using the 1.8 (and above) Mojang API import.
     *
     * @param uuid UUID
     * @param username Username
     * @return 1.8 (and above) Mojang Game Profile.
     */
    public static Object getGameProfile(UUID uuid, String username) {
        return new GameProfile(uuid, username);
    }

    /**
     * Create a Wrapper for the Mojang Game Profile object.
     *
     * @param gameProfile Mojang Game profile
     * @return {@link WrappedGameProfile}
     */
    public static WrappedGameProfile getWrappedGameProfile(Object gameProfile) {
        GameProfile gp = (GameProfile) gameProfile;
        return new WrappedGameProfile(gp.getId(), gp.getName(), gp.isLegacy());
    }
}
