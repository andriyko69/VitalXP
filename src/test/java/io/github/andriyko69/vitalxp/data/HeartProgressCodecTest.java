package io.github.andriyko69.vitalxp.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HeartProgressCodecTest {
    @Test
    void codecRoundTripsStableFields() {
        HeartProgress expected = new HeartProgress(3, 17);

        var encoded = HeartProgress.CODEC.encodeStart(NbtOps.INSTANCE, expected).getOrThrow();
        HeartProgress decoded = HeartProgress.CODEC.parse(NbtOps.INSTANCE, encoded).getOrThrow();

        assertEquals(expected, decoded);
        assertTrue(encoded.toString().contains("earned_tiers"));
        assertTrue(encoded.toString().contains("highest_level"));
    }

    @Test
    void missingFieldsUseZeroDefaults() {
        HeartProgress decoded = HeartProgress.CODEC.parse(NbtOps.INSTANCE, new CompoundTag()).getOrThrow();

        assertEquals(HeartProgress.EMPTY, decoded);
    }

    @Test
    void negativeValuesAreClamped() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("earned_tiers", -4);
        tag.putInt("highest_level", -12);

        HeartProgress decoded = HeartProgress.CODEC.parse(NbtOps.INSTANCE, tag).getOrThrow();

        assertEquals(HeartProgress.EMPTY, decoded);
    }

    @Test
    void malformedFieldFailsWithoutThrowing() {
        CompoundTag tag = new CompoundTag();
        tag.putString("earned_tiers", "not an integer");

        var result = HeartProgress.CODEC.parse(NbtOps.INSTANCE, tag);

        assertTrue(result.result().isEmpty());
        assertTrue(result.error().isPresent());
    }
}
