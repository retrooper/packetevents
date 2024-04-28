package com.github.retrooper.packetevents.adventure;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;

import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface GroupedAudience extends ForwardingAudience {
    GroupedAudience EVERYONE = GroupedAudience.of(PacketEvents.getAPI().getProtocolManager().getUsers());

    static GroupedAudience of(@NotNull Collection<User> users) {
        return new GroupedAudience() {
            @Override
            public @NotNull Collection<User> getUsers() {
                return users;
            }
        };
    }

    static GroupedAudience filtered(@NotNull Collection<User> users, @NotNull Predicate<User> filter) {
        Collection<User> filteredUsers = users.stream().filter(filter).collect(Collectors.toList());
        return new GroupedAudience() {
            @Override
            public @NotNull Collection<User> getUsers() {
                return filteredUsers;
            }
        };
    }

    static GroupedAudience filtered(@NotNull Predicate<User> filter) {
        return filtered(PacketEvents.getAPI().getProtocolManager().getUsers(), filter);
    }

    default void sendGroupedPacket(@NotNull PacketWrapper<?> packet) {
        for (User user : getUsers()) {
            user.sendPacket(packet);
        }
    }

    @Override
    default @NotNull Iterable<? extends Audience> audiences() {
        return getUsers();
    }

    @NotNull
    Collection<User> getUsers();
}
