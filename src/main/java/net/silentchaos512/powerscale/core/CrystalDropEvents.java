package net.silentchaos512.powerscale.core;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.silentchaos512.powerscale.Config;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.setup.PsAttachmentTypes;
import net.silentchaos512.powerscale.setup.PsTags;

@EventBusSubscriber(modid = PowerScale.MOD_ID)
public class CrystalDropEvents {
    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        var killer = event.getSource().getEntity();
        if (killer instanceof Player player) {
            for (ItemEntity itemEntity : event.getDrops()) {
                var itemStack = itemEntity.getItem();
                if (itemStack.is(PsTags.Items.CRYSTALS)) {
                    // Add a cooldown timer which will stop drops from the standard random drops pool for a few minutes
                    addCrystalCooldown(player);
                }
            }
        }
    }

    private static void addCrystalCooldown(Player player) {
        var time = (int) (Config.COMMON.crystalDropCooldown.get() * 1200);
        player.setData(PsAttachmentTypes.CRYSTAL_DROP_COOLDOWN, time);
        PowerScale.LOGGER.debug("Set crystal cooldown timer for {} ticks to {}", time, player.getScoreboardName());
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        var player = event.getEntity();
        int timer = player.getData(PsAttachmentTypes.CRYSTAL_DROP_COOLDOWN);
        if (timer > 0) {
            int newTime = timer - 1;
            player.setData(PsAttachmentTypes.CRYSTAL_DROP_COOLDOWN, newTime);
            if (newTime <= 0) {
                PowerScale.LOGGER.debug("Crystal cooldown time expired for {}", player.getScoreboardName());
            }
        }
    }
}
