package io.github.andriyko69.vitalxp.data;

import io.github.andriyko69.vitalxp.VitalXP;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Objects;
import java.util.function.Supplier;

public final class ModAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(net.neoforged.neoforge.registries.NeoForgeRegistries.ATTACHMENT_TYPES, VitalXP.MODID);

    public static final Supplier<AttachmentType<HeartProgress>> HEART_PROGRESS =
            ATTACHMENTS.register("heart_progress", () ->
                    AttachmentType.builder(() -> new HeartProgress(0, 0))
                            // We will copy/reset explicitly in PlayerEvent.Clone to respect RESET_ON_DEATH.
                            .build()
            );

    public static void register(ModContainer modContainer) {
        ATTACHMENTS.register(Objects.requireNonNull(modContainer.getEventBus()));
    }

    private ModAttachments() {}
}
