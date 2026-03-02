package io.github.andriyko69.vitalxp.gameplay;

import io.github.andriyko69.vitalxp.Config;
import io.github.andriyko69.vitalxp.data.HeartProgress;
import io.github.andriyko69.vitalxp.data.ModAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;

public final class VitalXpEvents {
    private VitalXpEvents() {
    }

    @SubscribeEvent
    public static void onLevelChange(PlayerXpEvent.LevelChange event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        HeartProgress prog = player.getData(ModAttachments.HEART_PROGRESS.get());

        int oldLevel = player.experienceLevel - event.getLevels();
        int newLevel = player.experienceLevel;

        int gained = HeartLogic.awardFromLevels(oldLevel, newLevel, prog);
        if (gained > 0) {
            HeartLogic.applyMaxHealth(player, prog);
            HeartLogic.onHeartGained(player, gained);
        }

        prog.setLastLevel(newLevel);
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        HeartProgress prog = player.getData(ModAttachments.HEART_PROGRESS.get());

        prog.setLastLevel(player.experienceLevel);
        HeartLogic.applyMaxHealth(player, prog);
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        HeartProgress prog = player.getData(ModAttachments.HEART_PROGRESS.get());
        prog.setLastLevel(player.experienceLevel);
        HeartLogic.applyMaxHealth(player, prog);
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        if (!(event.getEntity() instanceof ServerPlayer newPlayer)) return;
        if (!(event.getOriginal() instanceof ServerPlayer oldPlayer)) return;

        HeartProgress oldProg = oldPlayer.getData(ModAttachments.HEART_PROGRESS.get());
        HeartProgress newProg = newPlayer.getData(ModAttachments.HEART_PROGRESS.get());

        boolean wasDeath = event.isWasDeath();

        if (wasDeath && Config.resetOnDeath) {
            newProg.setTiers(0);
        } else {
            newProg.setTiers(oldProg.tiers());
        }

        newProg.setLastLevel(newPlayer.experienceLevel);
    }
}
