package net.silentchaos512.powerscale.core;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.silentchaos512.powerscale.Config;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.setup.PsAttachmentTypes;

public class PlayerDataHelper {
    @EventBusSubscriber(modid = PowerScale.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
    public static class Events {
        @SubscribeEvent
        public static void playerTickPost(PlayerTickEvent.Post event) {
            if (!Config.SERVER.quickToggleDifficulty.get()) return;

            var player = event.getEntity();
            if (player.tickCount % 20 != 0) return;

            var difficultyValue = player.getData(PsAttachmentTypes.DIFFICULTY);
            var newDifficultyValue = Config.COMMON.difficultyMutatorPerSecond
                    .withPlayer(player)
                    .evaluateDouble(difficultyValue, player);
            player.setData(PsAttachmentTypes.DIFFICULTY, newDifficultyValue);
        }
    }
}
