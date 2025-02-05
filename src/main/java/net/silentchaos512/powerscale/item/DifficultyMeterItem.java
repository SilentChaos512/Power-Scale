package net.silentchaos512.powerscale.item;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.silentchaos512.lib.util.MathUtils;
import net.silentchaos512.powerscale.Config;
import net.silentchaos512.powerscale.client.ClientData;

import javax.annotation.Nullable;

public class DifficultyMeterItem extends Item {
    public DifficultyMeterItem(Properties pProperties) {
        super(pProperties);
    }

    public static float getDifficultyScaleForModel(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        if (entity != null) {
            double maxDifficulty = Config.COMMON.difficultyLocalMax.get();
            double difficulty = MathUtils.clamp(ClientData.get().localDifficulty(), 0.0, maxDifficulty);
            return (float) (difficulty / maxDifficulty);
        }
        return 0f;
    }
}
