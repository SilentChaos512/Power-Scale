package net.silentchaos512.powerscale.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.silentchaos512.powerscale.core.EntityGroups;

import java.util.function.Supplier;

public class BonusDropsLootModifier extends LootModifier {
    public static final Supplier<MapCodec<BonusDropsLootModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.mapCodec(
                    inst -> codecStart(inst).apply(inst, BonusDropsLootModifier::new)
            )
    );

    public BonusDropsLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        Entity thisEntity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (thisEntity == null) {
            return generatedLoot;
        }

        ObjectArrayList<ItemStack> ret = new ObjectArrayList<>(generatedLoot);

        var server = context.getLevel().getServer();
        var lootTableLookup = server.reloadableRegistries().lookup().lookup(Registries.LOOT_TABLE).orElseThrow();
        var entityGroup = EntityGroups.from(thisEntity);
        if (entityGroup != EntityGroups.NONE) {
            lootTableLookup.get(entityGroup.getLootTable()).ifPresent(lootTable -> {
                // Must use raw method to prevent stack overflow
                //noinspection deprecation
                lootTable.value().getRandomItemsRaw(context, ret::add);
            });
        }

        return ret;
    }
}
