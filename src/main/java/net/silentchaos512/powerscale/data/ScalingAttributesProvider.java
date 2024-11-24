package net.silentchaos512.powerscale.data;

import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.silentchaos512.powerscale.Config;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.core.scalingattribute.MobScalingSet;
import net.silentchaos512.powerscale.core.scalingattribute.MutatorSet;
import net.silentchaos512.powerscale.core.scalingattribute.ScalingAttribute;
import net.silentchaos512.powerscale.setup.Const;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ScalingAttributesProvider implements DataProvider {
    private final PackOutput packOutput;

    public ScalingAttributesProvider(PackOutput packOutput) {
        this.packOutput = packOutput;
    }

    private Map<ResourceLocation, ScalingAttribute> getScalingAttributes() {
        return Util.make(() -> {
            Map<ResourceLocation, ScalingAttribute> ret = new LinkedHashMap<>();
            ret.put(Const.ATTACK_DAMAGE,
                    new ScalingAttribute(
                            Attributes.ATTACK_DAMAGE,
                            new MobScalingSet(Config.COMMON.defaultScalingAttributeAttackDamage),
                            new MutatorSet(Config.COMMON.defaultScalingAttributeAttackDamage)
                    )
            );
            ret.put(Const.MAX_HEALTH,
                    new ScalingAttribute(
                            Attributes.MAX_HEALTH,
                            new MobScalingSet(Config.COMMON.defaultScalingAttributeMaxHealth),
                            new MutatorSet(Config.COMMON.defaultScalingAttributeMaxHealth)
                    )
            );
            ret.put(Const.MOVEMENT_SPEED,
                    new ScalingAttribute(
                            Attributes.MOVEMENT_SPEED,
                            new MobScalingSet(Config.COMMON.defaultScalingAttributeMovementSpeed),
                            new MutatorSet(Config.COMMON.defaultScalingAttributeMovementSpeed)
                    )
            );
            return ret;
        });
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        Path outputFolder = this.packOutput.getOutputFolder();
        Set<ResourceLocation> set = Sets.newHashSet();
        List<CompletableFuture<?>> list = new ArrayList<>();

        this.getScalingAttributes().forEach((id, scalingAttribute) -> {
            if (!set.add(id)) {
                throw new IllegalStateException("Duplicate scaling attribute: " + id);
            }
            Path path = outputFolder.resolve(String.format("data/%s/scaling_attribute/%s.json", id.getNamespace(), id.getPath()));
            list.add(DataProvider.saveStable(cachedOutput, serialize(id, scalingAttribute), path));
        });

        return CompletableFuture.allOf(list.toArray(new CompletableFuture[0]));
    }

    private JsonElement serialize(ResourceLocation id, ScalingAttribute scalingAttribute) {
        var jsonElementDataResult = ScalingAttribute.CODEC.encodeStart(JsonOps.INSTANCE, scalingAttribute);
        if (jsonElementDataResult.isError()) {
            PowerScale.LOGGER.error("Something went wrong when serializing scaling attribute \"{}\"", id);
        }
        return jsonElementDataResult.getOrThrow();
    }

    @Override
    public String getName() {
        return PowerScale.MOD_NAME + ": Scaling Attributes";
    }
}
