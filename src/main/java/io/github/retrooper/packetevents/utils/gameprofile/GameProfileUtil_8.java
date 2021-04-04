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
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * 1.8 (and above) Mojang Game Profile util using the 1.8 (and above) Mojang API import location.
 *
 * @author retrooper
 * @since 1.6.8.2
 */
class GameProfileUtil_8 {

    public static Object getGameProfile(UUID uuid, String username) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            Object entityHuman = NMSUtils.entityHumanClass.cast(NMSUtils.getEntityPlayer(player));
            WrappedPacket wrappedEntityPlayer = new WrappedPacket(new NMSPacket(entityHuman), NMSUtils.entityHumanClass);
            return wrappedEntityPlayer.readObject(0, GameProfile.class);
        }
        else {
            return new GameProfile(uuid, username);
        }
    }

    public static WrappedGameProfile getWrappedGameProfile(Object gameProfile) {
        GameProfile gp = (GameProfile) gameProfile;
        return new WrappedGameProfile(gp.getId(), gp.getName(), gp.getProperties());
    }

    public static Object getProperty(String name, String value, String signature) {
        return new Property(name, value, signature);
    }

    public static WrappedProperty getWrappedProperty(Object property) {
        Property prop = (Property) property;
        return new WrappedProperty(prop.getName(), prop.getValue(), prop.getSignature());
    }

    public static Object getNewPropertyMap() {
        return new PropertyMap();
    }
}
