package net.silentchaos512.powerscale.loot.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.silentchaos512.powerscale.Config;
import net.silentchaos512.powerscale.setup.PsLoot;

public record CrystalDropChanceConfig(
        Holder<Enchantment> enchantment
) implements LootItemCondition {
    public static final MapCodec<CrystalDropChanceConfig> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Enchantment.CODEC.fieldOf("enchantment").forGetter(CrystalDropChanceConfig::enchantment)
            ).apply(instance, CrystalDropChanceConfig::new)
    );

    @Override
    public LootItemConditionType getType() {
        return PsLoot.CRYSTAL_DROP_CHANCE_CONFIG.get();
    }

    @Override
    public boolean test(LootContext lootContext) {
        Entity entity = lootContext.getParamOrNull(LootContextParams.ATTACKING_ENTITY);
        int enchantmentLevel = entity instanceof LivingEntity livingentity ? EnchantmentHelper.getEnchantmentLevel(this.enchantment, livingentity) : 0;
        float baseChance = Config.COMMON.crystalDropChance.get().floatValue();
        float enchantedBonusPerLevel = Config.COMMON.crystalDropChanceLootingBonus.get().floatValue();
        float chance = baseChance + enchantmentLevel * enchantedBonusPerLevel;
        return lootContext.getRandom().nextFloat() < chance;
    }

    public static LootItemCondition.Builder configWithLootingBonus(HolderLookup.Provider registries) {
        HolderLookup.RegistryLookup<Enchantment> registryLookup = registries.lookupOrThrow(Registries.ENCHANTMENT);
        return () -> new CrystalDropChanceConfig(registryLookup.getOrThrow(Enchantments.LOOTING));
    }
}
