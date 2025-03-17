package net.silentchaos512.powerscale.loot.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.silentchaos512.powerscale.Config;
import net.silentchaos512.powerscale.setup.PsLoot;

public class BossCrystalDropConfig implements LootItemCondition {
    public static final BossCrystalDropConfig INSTANCE = new BossCrystalDropConfig();
    public static final MapCodec<BossCrystalDropConfig> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public LootItemConditionType getType() {
        return PsLoot.BOSS_CRYSTAL_DROPS_CONFIG.get();
    }

    @Override
    public boolean test(LootContext lootContext) {
        return Config.COMMON.bossesDropCrystals.get();
    }

    public static LootItemCondition.Builder builder() {
        return () -> INSTANCE;
    }
}
