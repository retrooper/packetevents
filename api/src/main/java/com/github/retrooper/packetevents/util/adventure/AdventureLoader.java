/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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

package com.github.retrooper.packetevents.util.adventure;

import com.github.retrooper.packetevents.util.reflection.Reflection;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.CodeSource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NullMarked
@ApiStatus.Internal
public final class AdventureLoader {

    /**
     * A list of all adventure dependencies which may be jar-in-jar'ed, depending on the platform.<br/>
     * Note that we may load a dependency even if it is present as an older version, as we need some newer classes (e.g. dialogs).
     * This is not ideal, though we don't want to limit ourselves to just adventure v4 API. Also, this shouldn't cause
     * any major issues because we dump everything into the parent classloader which should prevent any class conflicts.
     * <p>
     * The thing which does cause lots of issues is version conflicts - we include a pretty new version of adventure,
     * while some users may run 1.16.5 servers, which causes incompatibilities between the two adventure libs.
     */
    private static final List<Dependency> DEPENDENCIES = Arrays.asList(
            new Dependency("examination-api", "net.kyori.examination.Examinable"),
            new Dependency("examination-string", "net.kyori.examination.string.StringExaminer"),
            new Dependency("option", "net.kyori.option.Option"),
            new Dependency("adventure-key", "net.kyori.adventure.key.Key"),
            new Dependency("adventure-api", "net.kyori.adventure.text.ObjectComponent"),
            new Dependency("adventure-nbt", "net.kyori.adventure.nbt.BinaryTag"),
            new Dependency("adventure-text-serializer-json", "net.kyori.adventure.text.serializer.json.JSONComponentSerializer"),
            new Dependency("adventure-text-serializer-gson", "net.kyori.adventure.text.serializer.gson.GsonComponentSerializer"),
            new Dependency("adventure-text-serializer-json-legacy-impl", "net.kyori.adventure.text.serializer.json.legacyimpl.NBTLegacyHoverEventSerializer"),
            new Dependency("adventure-text-serializer-legacy", "net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer")
    );

    private AdventureLoader() {
    }

    public static Set<Path> injectAll(URLClassLoader classLoader) {
        String adventureVersion = AdventureVersionDetector.detectAdventureVersion();
        System.out.println("ADV VER: " + adventureVersion);
        Set<Path> injectedJars = new HashSet<>();
        for (Dependency dependency : DEPENDENCIES) {
            if (!dependency.isAvailable()) {
                injectedJars.add(dependency.inject(classLoader));
            }
        }
        return injectedJars;
    }

    public static void uninjectAll(URLClassLoader loader, Set<Path> injectedJars) {
        for (Dependency dependency : DEPENDENCIES) {
            CodeSource source = dependency.getCodeSource();
            if (source == null) {
                continue;
            }
            Path path;
            try {
                URI locationUri = source.getLocation().toURI();
                if (!"file".equals(locationUri.getScheme())) {
                    continue; // ignore
                }
                path = FileSystems.getDefault().getPath(locationUri.getPath());
            } catch (URISyntaxException ignored) {
                continue; // ignore
            }
            if (injectedJars.contains(path)) {
                dependency.uninject(loader, source.getLocation());
            }
        }
    }

    private static final class Dependency {

        private static final MethodHandle GET_UCP, ADD_URL;

        static {
            MethodHandles.Lookup trustedLookup;
            try {
                // why does this still work?
                Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
                Field unsafeField = unsafeClass.getDeclaredField("theUnsafe");
                unsafeField.setAccessible(true);
                Object unsafe = unsafeField.get(null);

                // unsafe is semi-internal now (errors on compile), so access it via reflection
                Method getObject = unsafeClass.getMethod("getObject", Object.class, long.class);
                Method staticFieldBase = unsafeClass.getMethod("staticFieldBase", Field.class);
                Method staticFieldOffset = unsafeClass.getMethod("staticFieldOffset", Field.class);

                // get trusted methodhandles lookup for invoking jvm classloader internals
                Field trustedLookupField = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
                trustedLookup = (MethodHandles.Lookup) getObject.invoke(unsafe,
                        staticFieldBase.invoke(unsafe, trustedLookupField),
                        (long) staticFieldOffset.invoke(unsafe, trustedLookupField));
            } catch (ReflectiveOperationException exception) {
                throw new RuntimeException("Error while getting trusted method lookup", exception);
            }

            try {
                Class<?> urlClassPathClass = URLClassLoader.class.getDeclaredField("ucp").getType();
                GET_UCP = trustedLookup.findGetter(URLClassLoader.class, "ucp", urlClassPathClass);
                ADD_URL = trustedLookup.findVirtual(urlClassPathClass, "addURL", MethodType.methodType(void.class, URL.class));
            } catch (ReflectiveOperationException exception) {
                throw new RuntimeException("Error while looking up URLClassLoader injection methods", exception);
            }
        }

        private final String name;
        private final String className;

        public Dependency(String name, String className) {
            this.name = name;
            this.className = className;
        }

        public String getJarName() {
            return "/assets/libs/" + this.name + ".jar";
        }

        public @Nullable CodeSource getCodeSource() {
            Class<?> clazz = Reflection.getClassByNameWithoutException(this.className);
            return clazz == null ? null : clazz.getProtectionDomain().getCodeSource();
        }

        public boolean isAvailable() {
            return Reflection.getClassByNameWithoutException(this.className) != null;
        }

        public Path inject(URLClassLoader classLoader) {
            try (InputStream resource = Dependency.class.getResourceAsStream(this.getJarName())) {
                if (resource == null) {
                    throw new IllegalStateException("Can't find " + this.getJarName() + " in classpath of " + Dependency.class.getProtectionDomain().getCodeSource().getLocation());
                }
                return this.inject(resource, classLoader);
            } catch (IOException exception) {
                throw new RuntimeException("Failed to read " + this.name + " from classpath", exception);
            }
        }

        public Path inject(InputStream resource, URLClassLoader classLoader) {
            Path tempFile;
            try {
                // copy to external temp file
                tempFile = Files.createTempFile("packetevents_" + this.name + "_", ".jar");
                tempFile.toFile().deleteOnExit();
                Files.copy(resource, tempFile, StandardCopyOption.REPLACE_EXISTING);
                URL tempUrl = tempFile.toUri().toURL();

                // append to classloader
                ADD_URL.invoke(GET_UCP.invoke(classLoader), tempUrl);
            } catch (Throwable exception) {
                throw new RuntimeException(exception);
            }

            // ensure we successfully loaded everything
            try {
                classLoader.loadClass(this.className);
            } catch (Throwable ignored) {
                throw new IllegalStateException("Failed to load dependency '" + this.name + "' into classloader " + classLoader);
            }
            System.out.println("LOADED " + this.name);
            return tempFile;
        }

        public void uninject(URLClassLoader classLoader, URL url) {
            // TODO though not a priority
        }
    }
}
