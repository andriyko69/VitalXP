package io.github.andriyko69.vitalxp;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;


@EventBusSubscriber(modid = VitalXP.MODID)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue BASE_VITALITY = BUILDER.comment("Base health points for players. 20 = 10 hearts").defineInRange("baseVitality", 20, 1, 255);
    private static final ModConfigSpec.IntValue HEALTH_CAP = BUILDER.comment("Maximum health points players can have. 20 = 10 hearts").defineInRange("healthCap", 40, 1, 255);
    private static final ModConfigSpec.IntValue LEVEL_INTERVAL = BUILDER.comment("Number of levels required to gain 1 heart").defineInRange("levelInterval", 5, 1, 255);
    private static final ModConfigSpec.BooleanValue PLAY_UPGRADE_SOUND = BUILDER.comment("Whether to play the level-up sound when gaining a heart").define("playUpgradeSound", true);
    private static final ModConfigSpec.BooleanValue RESET_ON_DEATH = BUILDER.comment("Whether to reset gained hearts on death").define("resetProgressOnDeath", true);
    private static final ModConfigSpec.BooleanValue RESTORE_ON_UPGRADE = BUILDER.comment("Whether to restore health when gaining a heart").define("restoreHealthOnUpgrade", true);
    private static final ModConfigSpec.BooleanValue CONSUME_XP_ON_UPGRADE = BUILDER.comment("Whether to consume XP levels when gaining a heart").define("consumeXpOnUpgrade", false);
    private static final ModConfigSpec.IntValue XP_COST_PER_UPGRADE = BUILDER.comment("Number of XP levels consumed per heart gain. Set to -1 to consume all levels").defineInRange("xpCostPerUpgrade", 1, -1, 255);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int baseVitality;
    public static int healthCap;
    public static int levelInterval;
    public static boolean playUpgradeSound;
    public static boolean resetOnDeath;
    public static boolean restoreOnUpgrade;
    public static boolean consumeXpOnUpgrade;
    public static int xpCostPerUpgrade;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        baseVitality = BASE_VITALITY.get();
        healthCap = HEALTH_CAP.get();
        levelInterval = LEVEL_INTERVAL.get();
        playUpgradeSound = PLAY_UPGRADE_SOUND.get();
        resetOnDeath = RESET_ON_DEATH.get();
        restoreOnUpgrade = RESTORE_ON_UPGRADE.get();
        consumeXpOnUpgrade = CONSUME_XP_ON_UPGRADE.get();
        xpCostPerUpgrade = XP_COST_PER_UPGRADE.get();
    }
}
