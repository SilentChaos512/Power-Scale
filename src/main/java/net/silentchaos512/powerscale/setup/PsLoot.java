package net.silentchaos512.powerscale.setup;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.loot.BonusDropsLootModifier;
import net.silentchaos512.powerscale.loot.condition.MobProperties;

import java.util.function.Supplier;

public class PsLoot {
    static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPES =
            DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, PowerScale.MOD_ID);
    static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, PowerScale.MOD_ID);

    public static final Supplier<LootItemConditionType> MOB_PROPERTIES = LOOT_CONDITION_TYPES.register(
            "mob_properties",
            () -> new LootItemConditionType(MobProperties.CODEC)
    );

    public static final Supplier<MapCodec<BonusDropsLootModifier>> MOB_DROPS = LOOT_MODIFIERS.register(
            "bonus_drops",
            BonusDropsLootModifier.CODEC
    );
}
