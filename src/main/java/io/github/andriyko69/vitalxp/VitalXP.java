package io.github.andriyko69.vitalxp;

import io.github.andriyko69.vitalxp.data.ModAttachments;
import io.github.andriyko69.vitalxp.gameplay.VitalXpEvents;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(VitalXP.MODID)
public class VitalXP {
    public static final String MODID = "vitalxp";

    public VitalXP(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        ModAttachments.register(modContainer);

        NeoForge.EVENT_BUS.register(VitalXpEvents.class);
    }

}
