package io.github.retrooper.packetevents.utils;

import java.util.Optional;

public class ConditionalValue<T, K> {
    private final Optional<T> left;
    private final Optional<K> right;
    private ConditionalValue(Optional<T> left, Optional<K> right) {
        this.left = left;
        this.right = right;
    }
    public Optional<T> left() {
        return left;
    }
    public Optional<K> right() {
        return right;
    }

    @Override
    public String toString() {
        return left.map(t -> "ConditionalValue {left: " + t.toString() + "}")
                .orElseGet(() -> "ConditionalValue {right: " + right.get().toString() + "}");
    }

    public static <L, R> ConditionalValue<L, R> makeLeft(L left) {
        return new ConditionalValue<>(Optional.of(left), Optional.empty());
    }

    public static <L, R> ConditionalValue<L, R> makeRight(R right) {
        return new ConditionalValue<>(Optional.empty(), Optional.of(right));
    }
}
