package io.github.andriyko69.vitalxp.gameplay;

import io.github.andriyko69.vitalxp.Config;
import io.github.andriyko69.vitalxp.data.HeartProgress;
import io.github.andriyko69.vitalxp.data.ModAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public final class VitalXpEvents {
    private VitalXpEvents() {
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        var attachment = ModAttachments.HEART_PROGRESS.get();
        HeartProgress progress = player.getData(attachment);
        ProgressionLogic.Reconciliation result = ProgressionLogic.reconcile(
                progress,
                player.experienceLevel,
                Config.levelInterval
        );

        if (!result.progress().equals(progress)) {
            player.setData(attachment, result.progress());
        }

        HeartLogic.applyMaxHealth(player, result.progress(), false);
        HeartLogic.onHeartGained(player, result.gainedTiers());
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        var attachment = ModAttachments.HEART_PROGRESS.get();
        HeartProgress progress;
        if (player.hasData(attachment)) {
            progress = player.getData(attachment);
        } else {
            progress = HeartLogic.migrateLegacyProgress(player);
        }

        ProgressionLogic.Reconciliation result = ProgressionLogic.reconcile(
                progress,
                player.experienceLevel,
                Config.levelInterval
        );
        player.setData(attachment, result.progress());

        // Force replacement so a legacy permanent modifier is removed from saved attribute data.
        HeartLogic.applyMaxHealth(player, result.progress(), true);
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        HeartProgress progress = player.getData(ModAttachments.HEART_PROGRESS.get());
        HeartLogic.applyMaxHealth(player, progress, true);

        if (!event.isEndConquered()) {
            player.setHealth(player.getMaxHealth());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onClone(PlayerEvent.Clone event) {
        if (!(event.getEntity() instanceof ServerPlayer newPlayer)) {
            return;
        }
        if (!(event.getOriginal() instanceof ServerPlayer oldPlayer)) {
            return;
        }

        HeartProgress oldProgress = oldPlayer.getData(ModAttachments.HEART_PROGRESS.get());
        HeartProgress newProgress;
        if (event.isWasDeath() && Config.resetOnDeath) {
            newProgress = new HeartProgress(0, Math.max(0, newPlayer.experienceLevel));
        } else {
            newProgress = oldProgress;
        }

        newPlayer.setData(ModAttachments.HEART_PROGRESS.get(), newProgress);
    }
}
