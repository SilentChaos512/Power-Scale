package net.silentchaos512.powerscale.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.silentchaos512.powerscale.PowerScale;

public class PsTags {
    public static final class Items {
        public static final TagKey<Item> FLASK_GEMS = mod("flask_gems");

        private static TagKey<Item> mod(String path) {
            return TagKey.create(Registries.ITEM, PowerScale.getId(path));
        }
    }
}
