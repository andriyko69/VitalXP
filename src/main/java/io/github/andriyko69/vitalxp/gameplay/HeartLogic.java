package io.github.andriyko69.vitalxp.gameplay;

import io.github.andriyko69.vitalxp.Config;
import io.github.andriyko69.vitalxp.VitalXP;
import io.github.andriyko69.vitalxp.data.HeartProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public final class HeartLogic {
    private static final ResourceLocation HEALTH_MOD_ID = ResourceLocation.fromNamespaceAndPath(VitalXP.MODID, "bonus_health");
    private static final double VANILLA_MAX_HEALTH = 20.0;

    private HeartLogic() {
    }

    public static HeartProgress migrateLegacyProgress(ServerPlayer player) {
        int legacyTiers = 0;
        var attribute = player.getAttribute(Attributes.MAX_HEALTH);
        if (attribute != null) {
            AttributeModifier modifier = attribute.getModifier(HEALTH_MOD_ID);
            if (modifier != null && modifier.operation() == AttributeModifier.Operation.ADD_VALUE) {
                legacyTiers = ProgressionLogic.inferLegacyTiers(modifier.amount(), Config.baseVitality);
            }
        }

        return ProgressionLogic.initialProgress(player.experienceLevel, Config.levelInterval, legacyTiers);
    }

    /**
     * Applies VitalXP's deterministic, vanilla-relative max-health contribution.
     */
    public static void applyMaxHealth(ServerPlayer player, HeartProgress progress, boolean forceTransientReplacement) {
        long targetVitality = (long) Config.baseVitality + (long) progress.earnedTiers() * 2L;
        if (Config.healthCap != -1) {
            targetVitality = Math.min(targetVitality, Config.healthCap);
        }
        double modifierAmount = targetVitality - VANILLA_MAX_HEALTH;

        var attribute = player.getAttribute(Attributes.MAX_HEALTH);
        if (attribute == null) {
            return;
        }

        AttributeModifier existing = attribute.getModifier(HEALTH_MOD_ID);
        boolean matches = existing != null
                && existing.operation() == AttributeModifier.Operation.ADD_VALUE
                && Double.compare(existing.amount(), modifierAmount) == 0;

        if (forceTransientReplacement || !matches) {
            attribute.removeModifier(HEALTH_MOD_ID);
            attribute.addTransientModifier(new AttributeModifier(
                    HEALTH_MOD_ID,
                    modifierAmount,
                    AttributeModifier.Operation.ADD_VALUE
            ));
        }

        if (player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }
    }

    public static void onHeartGained(ServerPlayer player, int gainedTiers) {
        if (gainedTiers <= 0) {
            return;
        }

        if (Config.playUpgradeSound) {
            player.level().playSound(
                    null,
                    player.blockPosition(),
                    SoundEvents.PLAYER_LEVELUP,
                    SoundSource.PLAYERS,
                    0.75f,
                    1.0f
            );
        }

        if (Config.consumeXpOnUpgrade) {
            int availableLevels = Math.max(0, player.experienceLevel);
            long requestedCost = Config.xpCostPerUpgrade == -1
                    ? availableLevels
                    : (long) Config.xpCostPerUpgrade * gainedTiers;
            int consumedLevels = (int) Math.min(requestedCost, availableLevels);
            if (consumedLevels > 0) {
                player.giveExperienceLevels(-consumedLevels);
            }
        }

        if (Config.restoreOnUpgrade) {
            player.setHealth(player.getMaxHealth());
        }
    }
}
