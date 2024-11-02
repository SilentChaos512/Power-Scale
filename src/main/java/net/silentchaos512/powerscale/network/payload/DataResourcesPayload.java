package net.silentchaos512.powerscale.network.payload;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public interface DataResourcesPayload<T> {
    Map<ResourceLocation, T> values();
}
