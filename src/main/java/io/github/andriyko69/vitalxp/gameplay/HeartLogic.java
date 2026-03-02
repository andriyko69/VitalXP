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

    private HeartLogic() {
    }

    /**
     * Computes how many heart-tiers were gained due to a level increase and updates progress.
     */
    public static int awardFromLevels(int oldLevel, int newLevel, HeartProgress prog) {
        if (newLevel <= oldLevel) return 0;

        int interval = Config.levelInterval;

        int oldMilestones = oldLevel / interval;
        int newMilestones = newLevel / interval;

        int gained = Math.max(0, newMilestones - oldMilestones);
        if (gained > 0) prog.setTiers(prog.tiers() + gained);

        return gained;
    }

    /**
     * Applies max health based on config + tiers (1 tier = +1 heart = +2 HP).
     */
    public static void applyMaxHealth(ServerPlayer player, HeartProgress prog) {
        int targetHp = Config.baseVitality + (prog.tiers() * 2);
        if (Config.healthCap != -1) targetHp = Math.min(targetHp, Config.healthCap);

        var attr = player.getAttribute(Attributes.MAX_HEALTH);
        if (attr == null) return;

        // Vanilla base is 20 HP. We offset from that.
        double add = targetHp - 20.0;

        AttributeModifier existing = attr.getModifier(HEALTH_MOD_ID);
        if (existing != null) attr.removeModifier(existing);

        attr.addPermanentModifier(new AttributeModifier(
                HEALTH_MOD_ID,
                add,
                AttributeModifier.Operation.ADD_VALUE
        ));

        // Ensure current HP isn't above new max
        if (player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }
    }

    public static void onHeartGained(ServerPlayer player, int gainedHearts) {
        if (gainedHearts <= 0) return;

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
            int cost = Config.xpCostPerUpgrade;
            int totalCost = (cost == -1) ? player.experienceLevel : (cost * gainedHearts);
            if (totalCost != 0) player.giveExperienceLevels(-totalCost);
        }

        if (Config.restoreOnUpgrade) {
            player.setHealth(player.getMaxHealth());
        }
    }
}
