package io.github.andriyko69.vitalxp.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record HeartProgress(int earnedTiers, int highestLevel) {
    private static final Codec<Integer> NON_NEGATIVE_INT = Codec.INT.xmap(
            value -> Math.max(0, value),
            value -> Math.max(0, value)
    );

    public static final Codec<HeartProgress> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            NON_NEGATIVE_INT.optionalFieldOf("earned_tiers", 0).forGetter(HeartProgress::earnedTiers),
            NON_NEGATIVE_INT.optionalFieldOf("highest_level", 0).forGetter(HeartProgress::highestLevel)
    ).apply(instance, HeartProgress::new));

    public static final HeartProgress EMPTY = new HeartProgress(0, 0);

    public HeartProgress {
        earnedTiers = Math.max(0, earnedTiers);
        highestLevel = Math.max(0, highestLevel);
    }
}
