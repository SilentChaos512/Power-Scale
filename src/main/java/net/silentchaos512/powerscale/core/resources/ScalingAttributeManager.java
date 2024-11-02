package net.silentchaos512.powerscale.core.resources;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.core.scalingattribute.ScalingAttribute;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ScalingAttributeManager extends DataResourceManager<ScalingAttribute> {
    private final Map<Holder<Attribute>, ScalingAttribute> attributeMap = Collections.synchronizedMap(new HashMap<>());
    public ScalingAttributeManager() {
        super(
                ScalingAttribute.CODEC,
                ScalingAttributeManager.ResourceJsonException::new,
                "scaling attribute",
                "scaling_attribute",
                PowerScale.LOGGER,
                "Scaling Attribute Manager",
                PowerScale.MOD_NAME
        );
    }

    @Nullable
    public ScalingAttribute get(Holder<Attribute> attribute) {
        synchronized (this.attributeMap) {
            return this.attributeMap.get(attribute);
        }
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        super.onResourceManagerReload(resourceManager);

        synchronized (this.attributeMap) {
            this.attributeMap.clear();
            for (ScalingAttribute scalingAttribute : this) {
                this.attributeMap.put(scalingAttribute.attribute(), scalingAttribute);
            }
        }
    }

    static class ResourceJsonException extends RuntimeException {
        public ResourceJsonException(ResourceLocation name, String packName, Throwable cause) {
            super("Error loading \"" + name + "\" from pack \"" + packName + "\": " + cause.getMessage(), cause);
        }
    }
}
