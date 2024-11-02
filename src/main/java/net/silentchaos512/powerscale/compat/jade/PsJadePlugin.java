package net.silentchaos512.powerscale.compat.jade;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.silentchaos512.powerscale.PowerScale;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class PsJadePlugin implements IWailaPlugin {
    public static final ResourceLocation MOB = PowerScale.getId("mob");

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(PsJadeMobComponentProvider.INSTANCE, Mob.class);
    }
}
