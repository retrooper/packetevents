/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2024 KyoriPowered
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
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JSONOptions;
import net.kyori.adventure.util.Services;
import net.kyori.option.OptionState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

final class GsonComponentSerializerImpl implements GsonComponentSerializer {
    private static final Optional<Provider> SERVICE = Services.service(Provider.class);
    static final Consumer<Builder> BUILDER = SERVICE
            .map(Provider::builder)
            .orElseGet(() -> builder -> {
                // NOOP
            });

    // We cannot store these fields in GsonComponentSerializerImpl directly due to class initialisation issues.
    static final class Instances {
        // packetevents patch start
        static final GsonComponentSerializer INSTANCE = SERVICE
                .map(Provider::gson)
                .orElseGet(() -> new GsonComponentSerializerImpl(JSONOptions.byDataVersion(), null, null));
        static final GsonComponentSerializer LEGACY_INSTANCE = SERVICE
                .map(Provider::gsonLegacy)
                .orElseGet(() -> new GsonComponentSerializerImpl(JSONOptions.byDataVersion().at(2525 /* just before 1.16 */), null, null));
        // packetevents patch end
    }

    private final Gson serializer;
    private final UnaryOperator<GsonBuilder> populator;
    private final net.kyori.adventure.text.serializer.json.@Nullable LegacyHoverEventSerializer legacyHoverSerializer;
    // packetevents patch start
    private final BackwardCompatUtil.@Nullable ShowAchievementToComponent compatShowAchievement;
    // packetevents patch end
    private final OptionState flags;

    // packetevents patch start
    GsonComponentSerializerImpl(
            final OptionState flags,
            final net.kyori.adventure.text.serializer.json.@Nullable LegacyHoverEventSerializer legacyHoverSerializer,
            final BackwardCompatUtil.@Nullable ShowAchievementToComponent compatShowAchievement) {
        this.flags = flags;
        this.legacyHoverSerializer = legacyHoverSerializer;
        this.compatShowAchievement = compatShowAchievement;
        this.populator = builder -> {
            builder.registerTypeAdapterFactory(new SerializerFactory(flags, legacyHoverSerializer, compatShowAchievement));
            return builder;
        };
        this.serializer = this.populator.apply(
                new GsonBuilder()
                        .disableHtmlEscaping() // to be consistent with vanilla
        ).create();
    }
    // packetevents patch end

    @Override
    public @NotNull Gson serializer() {
        return this.serializer;
    }

    @Override
    public @NotNull UnaryOperator<GsonBuilder> populator() {
        return this.populator;
    }

    @Override
    public @NotNull Component deserialize(final @NotNull String string) {
        final Component component = this.serializer().fromJson(string, Component.class);
        if (component == null) throw ComponentSerializerImpl.notSureHowToDeserialize(string);
        return component;
    }

    @Override
    public @Nullable Component deserializeOr(final @Nullable String input, final @Nullable Component fallback) {
        if (input == null) return fallback;
        final Component component = this.serializer().fromJson(input, Component.class);
        if (component == null) return fallback;
        return component;
    }

    @Override
    public @NotNull String serialize(final @NotNull Component component) {
        return this.serializer().toJson(component);
    }

    @Override
    public @NotNull Component deserializeFromTree(final @NotNull JsonElement input) {
        final Component component = this.serializer().fromJson(input, Component.class);
        if (component == null) throw ComponentSerializerImpl.notSureHowToDeserialize(input);
        return component;
    }

    @Override
    public @NotNull JsonElement serializeToTree(final @NotNull Component component) {
        return this.serializer().toJsonTree(component);
    }

    @Override
    public @NotNull Builder toBuilder() {
        return new BuilderImpl(this);
    }

    static final class BuilderImpl implements Builder {
        private OptionState flags = JSONOptions.byDataVersion(); // latest
        private net.kyori.adventure.text.serializer.json.@Nullable LegacyHoverEventSerializer legacyHoverSerializer;
        // packetevents patch start
        private BackwardCompatUtil.@Nullable ShowAchievementToComponent compatShowAchievement;
        // packetevents patch end

        BuilderImpl() {
            BUILDER.accept(this); // let service provider touch the builder before anybody else touches it
        }

        BuilderImpl(final GsonComponentSerializerImpl serializer) {
            this();
            this.flags = serializer.flags;
            this.legacyHoverSerializer = serializer.legacyHoverSerializer;
            // packetevents patch start
            this.compatShowAchievement = serializer.compatShowAchievement;
            // packetevents patch end
        }

        @Override
        public @NotNull Builder options(final @NotNull OptionState flags) {
            this.flags = requireNonNull(flags, "flags");
            return this;
        }

        @Override
        public @NotNull Builder editOptions(final @NotNull Consumer<OptionState.Builder> optionEditor) {
            final OptionState.Builder builder = OptionState.optionState()
                    .values(this.flags);
            requireNonNull(optionEditor, "flagEditor").accept(builder);
            this.flags = builder.build();
            return this;
        }

        @Override
        public @NotNull Builder legacyHoverEventSerializer(final net.kyori.adventure.text.serializer.json.@Nullable LegacyHoverEventSerializer serializer) {
            this.legacyHoverSerializer = serializer;
            return this;
        }

        // packetevents patch start
        public @NotNull Builder showAchievementToComponent(final BackwardCompatUtil.@Nullable ShowAchievementToComponent compatShowAchievement) {
            this.compatShowAchievement = compatShowAchievement;
            return this;
        }

        @Override
        public @NotNull GsonComponentSerializer build() {
            return new GsonComponentSerializerImpl(this.flags, this.legacyHoverSerializer, this.compatShowAchievement);
        }
    }
}
