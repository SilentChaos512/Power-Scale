package net.silentchaos512.powerscale.setup;

import net.minecraft.resources.ResourceLocation;
import net.silentchaos512.powerscale.PowerScale;
import org.jetbrains.annotations.NotNull;

public class Const {
    public static final ResourceLocation ATTACK_DAMAGE = id("attack_damage");
    public static final ResourceLocation MAX_HEALTH = id("max_health");
    public static final ResourceLocation MOVEMENT_SPEED = id("movement_speed");

    private static @NotNull ResourceLocation id(String path) {
        return PowerScale.getId(path);
    }
}
