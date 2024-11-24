package net.silentchaos512.powerscale.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.powerscale.PowerScale;

public class PsSounds {
    static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, PowerScale.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> CRYSTAL_USED = register("crystal_use");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(PowerScale.getId(name)));
    }
}
