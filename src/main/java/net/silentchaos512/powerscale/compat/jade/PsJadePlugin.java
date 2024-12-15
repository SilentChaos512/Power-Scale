package net.silentchaos512.powerscale.compat.jade;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.block.AlchemySetBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class PsJadePlugin implements IWailaPlugin {
    public static final ResourceLocation ALCHEMY_SET = PowerScale.getId("alchemy_set");
    public static final ResourceLocation MOB = PowerScale.getId("mob");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(PsJadeAlchemySetProvider.INSTANCE, AlchemySetBlock.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(PsJadeAlchemySetProvider.INSTANCE, AlchemySetBlock.class);
        registration.registerEntityComponent(PsJadeMobComponentProvider.INSTANCE, Mob.class);
    }
}
