package net.silentchaos512.powerscale.loot.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.silentchaos512.powerscale.Config;
import net.silentchaos512.powerscale.setup.PsLoot;

public class BlightCrystalDropConfig implements LootItemCondition {
    public static final BlightCrystalDropConfig INSTANCE = new BlightCrystalDropConfig();
    public static final MapCodec<BlightCrystalDropConfig> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public LootItemConditionType getType() {
        return PsLoot.BLIGHT_CRYSTAL_DROPS_CONFIG.get();
    }

    @Override
    public boolean test(LootContext lootContext) {
        return Config.COMMON.blightsDropCrystals.get();
    }

    public static LootItemCondition.Builder builder() {
        return () -> INSTANCE;
    }
}
