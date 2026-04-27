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

import com.github.retrooper.packetevents.util.PEVersion;
import com.github.retrooper.packetevents.util.reflection.Reflection;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import javax.net.ssl.HttpsURLConnection;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@NullMarked
@ApiStatus.Internal
public final class AdventureLoader {

    // TODO don't use maven central here
    private static final URI REPO_URI = URI.create("https://repo1.maven.org/maven2/");
    private static final String ADVENTURE_VERSION;

    static {
        // try to detect existing adventure version
        String adventureVersion = AdventureVersionDetector.detectAdventureVersion();
        if (adventureVersion != null) {
            ADVENTURE_VERSION = adventureVersion;
        } else {
            // adventure most likely doesn't exist on the classpath
            // yet, we just use a default adventure version in this case
            ADVENTURE_VERSION = "4.26.1";
        }
    }

    /**
     * A list of all adventure dependencies which may be jar-in-jar'ed, depending on the platform.<br/>
     * Note that we may load a dependency even if it is present as an older version, as we need some newer classes (e.g. dialogs).
     * This is not ideal, though we don't want to limit ourselves to just adventure v4 API. Also, this shouldn't cause
     * any major issues because we dump everything into the parent classloader which should prevent any class conflicts.
     * <p>
     * The thing which does cause lots of issues is version conflicts - we include a pretty new version of adventure,
     * while some users may run 1.16.5 servers, which causes incompatibilities between the two adventure libs.
     */
    private static final List<Dependency> DEPENDENCIES;

    static {
        List<Dependency> dependencies = new ArrayList<>();
        dependencies.add(new Dependency("net.kyori", "examination-api", "1.3.0", "net.kyori.examination.Examinable"));
        dependencies.add(new Dependency("net.kyori", "examination-string", "1.3.0", "net.kyori.examination.string.StringExaminer"));
        dependencies.add(new Dependency("net.kyori", "option", "1.1.0", "net.kyori.option.Option"));
        dependencies.add(new Dependency("net.kyori", "adventure-key", ADVENTURE_VERSION, "net.kyori.adventure.key.Key"));
        dependencies.add(new Dependency("net.kyori", "adventure-api", "4.26.1", "net.kyori.adventure.text.ObjectComponent"));
        dependencies.add(new Dependency("net.kyori", "adventure-nbt", ADVENTURE_VERSION, "net.kyori.adventure.nbt.BinaryTag"));
        if (!PEVersion.fromString(ADVENTURE_VERSION).isOlderThan(new PEVersion(4, 14, 0))) {
            dependencies.add(new Dependency("net.kyori", "adventure-text-serializer-json", ADVENTURE_VERSION, "net.kyori.adventure.text.serializer.json.JSONComponentSerializer"));
            dependencies.add(new Dependency("net.kyori", "adventure-text-serializer-json-legacy-impl", ADVENTURE_VERSION, "net.kyori.adventure.text.serializer.json.legacyimpl.NBTLegacyHoverEventSerializer"));
        }
        dependencies.add(new Dependency("net.kyori", "adventure-text-serializer-gson", ADVENTURE_VERSION, "net.kyori.adventure.text.serializer.gson.GsonComponentSerializer"));
        dependencies.add(new Dependency("net.kyori", "adventure-text-serializer-legacy", ADVENTURE_VERSION, "net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer"));
        DEPENDENCIES = Collections.unmodifiableList(dependencies);
    }

    private AdventureLoader() {
    }

    public static Set<Path> injectAll(URLClassLoader classLoader, Logger logger) {
        // check each adventure dependency
        Set<Path> injectedJars = new HashSet<>();
        for (Dependency dependency : DEPENDENCIES) {
            if (!dependency.isAvailable()) {
                logger.info("Loading dependency " + dependency + "...");
                injectedJars.add(dependency.inject(REPO_URI, classLoader));
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

        private static final MethodHandle GET_UCP, ADD_URL, GET_URLS;

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
                GET_URLS = trustedLookup.findVirtual(urlClassPathClass, "getURLs", MethodType.methodType(URL[].class));
            } catch (ReflectiveOperationException exception) {
                throw new RuntimeException("Error while looking up URLClassLoader injection methods", exception);
            }
        }

        private final String groupId;
        private final String artifactId;
        private final String version;

        private final String className;

        public Dependency(String groupId, String artifactId, String version, String className) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.version = version;
            this.className = className;
        }

        public @Nullable CodeSource getCodeSource() {
            Class<?> clazz = Reflection.getClassByNameWithoutException(this.className);
            return clazz == null ? null : clazz.getProtectionDomain().getCodeSource();
        }

        public boolean isAvailable() {
            return Reflection.getClassByNameWithoutException(this.className) != null;
        }

        public Path inject(URI repoUri, URLClassLoader classLoader) {
            // resolve maven-based artifact url at {repo}{groupId}/{artifactId}/{version}/{artifactId}-{version}.jar
            URI artifactUri = repoUri.resolve(this.groupId.replace('.', '/') + "/" + this.artifactId
                    + "/" + this.version + "/" + this.artifactId + "-" + this.version + ".jar");
            System.out.println("LOADING FROM " + artifactUri);
            // can't use java 11's http client because we still support java 8
            HttpsURLConnection connection;
            try {
                connection = (HttpsURLConnection) artifactUri.toURL().openConnection();
            } catch (IOException exception) {
                throw new RuntimeException("Failed to open connection to " + artifactUri, exception);
            }
            try {
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(10000);
                connection.connect();

                // inject jar directly from url into classpath
                try (InputStream resource = connection.getInputStream()) {
                    return this.inject(resource, classLoader);
                }
            } catch (IOException exception) {
                throw new RuntimeException("Failed to read from " + artifactUri, exception);
            } finally {
                connection.disconnect();
            }
        }

        public Path inject(InputStream resource, URLClassLoader classLoader) {
            Path tempFile;
            try {
                // copy to external temp file
                tempFile = Files.createTempFile("packetevents_" + this.artifactId + "_", ".jar");
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
                throw new IllegalStateException("Failed to load dependency '" + this.artifactId + "' into classloader " + classLoader);
            }
            System.out.println("LOADED " + this.artifactId);
            return tempFile;
        }

        public void uninject(URLClassLoader classLoader, URL url) {
            // TODO though not a priority
        }

        @Override
        public String toString() {
            return this.groupId + ":" + this.artifactId + ":" + this.version;
        }
    }
}
