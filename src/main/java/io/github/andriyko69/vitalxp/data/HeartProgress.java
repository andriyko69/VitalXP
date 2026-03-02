package io.github.andriyko69.vitalxp.data;

public final class HeartProgress {
    private int tiers;
    private int lastLevel;

    public HeartProgress(int tiers, int lastLevel) {
        this.tiers = Math.max(0, tiers);
        this.lastLevel = Math.max(0, lastLevel);
    }

    public int tiers() {
        return tiers;
    }

    public void setTiers(int tiers) {
        this.tiers = Math.max(0, tiers);
    }

    public int lastLevel() {
        return lastLevel;
    }

    public void setLastLevel(int lastLevel) {
        this.lastLevel = Math.max(0, lastLevel);
    }
}