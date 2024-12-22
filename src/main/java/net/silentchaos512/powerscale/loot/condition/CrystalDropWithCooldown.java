package net.silentchaos512.powerscale.loot.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.silentchaos512.powerscale.setup.PsAttachmentTypes;
import net.silentchaos512.powerscale.setup.PsLoot;

public class CrystalDropWithCooldown implements LootItemCondition {
    public static final CrystalDropWithCooldown INSTANCE = new CrystalDropWithCooldown();
    public static final MapCodec<CrystalDropWithCooldown> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public LootItemConditionType getType() {
        return PsLoot.CRYSTAL_DROP_WITH_COOLDOWN.get();
    }

    @Override
    public boolean test(LootContext lootContext) {
        Entity entity = lootContext.getParamOrNull(LootContextParams.ATTACKING_ENTITY);
        return entity instanceof Player && entity.getData(PsAttachmentTypes.CRYSTAL_DROP_COOLDOWN) <= 0;
    }

    public static LootItemCondition.Builder builder() {
        return () -> INSTANCE;
    }
}
