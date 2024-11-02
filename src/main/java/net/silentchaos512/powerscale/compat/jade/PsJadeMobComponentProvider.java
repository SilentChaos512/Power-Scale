package net.silentchaos512.powerscale.compat.jade;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.silentchaos512.lib.util.MathUtils;
import net.silentchaos512.powerscale.Config;
import net.silentchaos512.powerscale.network.payload.RequestMobDataPayload;
import net.silentchaos512.powerscale.setup.PsAttachmentTypes;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class PsJadeMobComponentProvider implements IEntityComponentProvider {
    public static final PsJadeMobComponentProvider INSTANCE = new PsJadeMobComponentProvider();

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor entityAccessor, IPluginConfig pluginConfig) {
        if (!Config.SERVER.quickToggleDifficulty.get()) return;

        var entity = entityAccessor.getEntity();
        if (hasRequiredData(entity)) {
            var level = entity.getData(PsAttachmentTypes.LEVEL);
            tooltip.add(Component.translatable("powerscale.level", level));
        } else {
            PacketDistributor.sendToServer(new RequestMobDataPayload(entity));
        }
    }

    private boolean hasRequiredData(Entity entity) {
        if (!entity.hasData(PsAttachmentTypes.LEVEL)) {
            return false;
        }
        var difficulty = entity.getData(PsAttachmentTypes.DIFFICULTY);
        return !MathUtils.doublesEqual(difficulty, 0.0);
    }

    @Override
    public ResourceLocation getUid() {
        return PsJadePlugin.MOB;
    }
}
