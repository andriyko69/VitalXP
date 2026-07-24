package io.github.andriyko69.vitalxp.gameplay;

import io.github.andriyko69.vitalxp.data.HeartProgress;

public final class ProgressionLogic {
    private static final double VANILLA_MAX_HEALTH = 20.0;

    private ProgressionLogic() {
    }

    public static Reconciliation reconcile(HeartProgress progress, int currentLevel, int levelInterval) {
        int observedLevel = Math.max(0, currentLevel);
        int interval = Math.max(1, levelInterval);

        if (observedLevel <= progress.highestLevel()) {
            return new Reconciliation(progress, 0);
        }

        int crossedMilestones = Math.max(
                0,
                observedLevel / interval - progress.highestLevel() / interval
        );
        int updatedTiers = saturatingAdd(progress.earnedTiers(), crossedMilestones);
        int gainedTiers = updatedTiers - progress.earnedTiers();

        return new Reconciliation(new HeartProgress(updatedTiers, observedLevel), gainedTiers);
    }

    public static Reconciliation reconcileToCurrentLevel(HeartProgress progress, int currentLevel, int levelInterval) {
        int observedLevel = Math.max(0, currentLevel);
        int interval = Math.max(1, levelInterval);
        int currentTiers = observedLevel / interval;
        int gainedTiers = Math.max(0, currentTiers - progress.earnedTiers());

        return new Reconciliation(new HeartProgress(currentTiers, observedLevel), gainedTiers);
    }

    public static HeartProgress initialProgress(int currentLevel, int levelInterval, int legacyTiers) {
        int observedLevel = Math.max(0, currentLevel);
        int interval = Math.max(1, levelInterval);
        int earnedTiers = Math.max(Math.max(0, legacyTiers), observedLevel / interval);
        int recoveredCheckpoint = saturatingMultiply(earnedTiers, interval);

        return new HeartProgress(earnedTiers, Math.max(observedLevel, recoveredCheckpoint));
    }

    public static int inferLegacyTiers(double modifierAmount, int baseVitality) {
        if (!Double.isFinite(modifierAmount)) {
            return 0;
        }

        double visibleTarget = VANILLA_MAX_HEALTH + modifierAmount;
        double inferred = Math.ceil((visibleTarget - baseVitality) / 2.0);
        if (!Double.isFinite(inferred) || inferred <= 0.0) {
            return 0;
        }
        if (inferred >= Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) inferred;
    }

    static int saturatingAdd(int left, int right) {
        long result = (long) left + right;
        return result >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result;
    }

    static int saturatingMultiply(int left, int right) {
        long result = (long) left * right;
        return result >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result;
    }

    public record Reconciliation(HeartProgress progress, int gainedTiers) {
    }
}
