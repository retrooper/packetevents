package io.github.retrooper.packetevents.utils;

import com.mojang.datafixers.util.Either;

import java.util.Optional;

public class MojangEitherUtil {
    public static <L, R> Either<L, R> makeLeft(L left) {
        return Either.left(left);
    }

    public static <L, R> Either<L, R> makeRight(R right) {
        return Either.right(right);
    }

    public static <L> Optional<L> getLeft(Object either) {
        return ((Either)either).left();
    }

    public static <R> Optional<R> getRight(Object either) {
        return ((Either)either).right();
    }
}
