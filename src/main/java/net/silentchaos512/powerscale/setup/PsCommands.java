package net.silentchaos512.powerscale.setup;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.command.DifficultyCommand;
import net.silentchaos512.powerscale.command.PlayerDataCommand;
import net.silentchaos512.powerscale.command.ScalingAttributeCommand;

@EventBusSubscriber(modid = PowerScale.MOD_ID)
public class PsCommands {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        var context = event.getBuildContext();

        DifficultyCommand.register(dispatcher);
        PlayerDataCommand.register(dispatcher);
        ScalingAttributeCommand.register(dispatcher, context);
    }
}
