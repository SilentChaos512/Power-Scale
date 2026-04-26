package net.silentchaos512.powerscale.setup;

import net.minecraft.resources.Identifier;
import net.silentchaos512.powerscale.PowerScale;
import org.jetbrains.annotations.NotNull;

public class Const {
    public static final Identifier ARROW_DAMAGE = id("arrow_damage");
    public static final Identifier ATTACK_DAMAGE = id("attack_damage");
    public static final Identifier MAX_HEALTH = id("max_health");
    public static final Identifier MOVEMENT_SPEED = id("movement_speed");

    private static @NotNull Identifier id(String path) {
        return PowerScale.getId(path);
    }
}
