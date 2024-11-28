package net.silentchaos512.powerscale.loot.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.silentchaos512.powerscale.setup.PsAttachmentTypes;
import net.silentchaos512.powerscale.setup.PsLoot;

import java.util.Optional;

public record MobProperties(
        Optional<IntRange> level,
        Optional<IntRange> difficulty,
        Optional<IntRange> blightTier
) implements LootItemCondition {
    public static final MapCodec<MobProperties> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    IntRange.CODEC.optionalFieldOf("level").forGetter(p -> p.level),
                    IntRange.CODEC.optionalFieldOf("difficulty").forGetter(p -> p.difficulty),
                    IntRange.CODEC.optionalFieldOf("blight_tier").forGetter(p -> p.blightTier)
            ).apply(instance, MobProperties::new)
    );

    @Override
    public LootItemConditionType getType() {
        return PsLoot.MOB_PROPERTIES.get();
    }

    @Override
    public boolean test(LootContext lootContext) {
        var entity = lootContext.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (entity == null) {
            return false;
        }

        if (this.level.isPresent() && !this.level.get().test(lootContext, entity.getData(PsAttachmentTypes.LEVEL))) {
            return false;
        }
        if (this.difficulty.isPresent() && !this.difficulty.get().test(lootContext, entity.getData(PsAttachmentTypes.DIFFICULTY).intValue())) {
            return false;
        }
        if (this.blightTier.isPresent() && !this.blightTier.get().test(lootContext, entity.getData(PsAttachmentTypes.BLIGHT_TIER))) {
            return false;
        }

        return true;
    }
}
