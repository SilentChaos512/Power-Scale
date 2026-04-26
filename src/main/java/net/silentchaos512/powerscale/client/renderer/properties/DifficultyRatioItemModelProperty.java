package net.silentchaos512.powerscale.client.renderer.properties;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import net.silentchaos512.powerscale.item.DifficultyMeterItem;
import net.silentchaos512.powerscale.setup.PsDataComponents;
import org.jspecify.annotations.Nullable;

public class DifficultyRatioItemModelProperty implements RangeSelectItemModelProperty {
    public static final DifficultyRatioItemModelProperty INSTANCE = new DifficultyRatioItemModelProperty();
    public static final MapCodec<DifficultyRatioItemModelProperty> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public float get(ItemStack stack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        var mode = stack.getOrDefault(PsDataComponents.DIFFICULTY_METER_MODE, DifficultyMeterItem.Mode.LOCAL);
        return DifficultyMeterItem.getDifficultyScaleForModel(mode);
    }

    @Override
    public MapCodec<? extends RangeSelectItemModelProperty> type() {
        return CODEC;
    }
}
