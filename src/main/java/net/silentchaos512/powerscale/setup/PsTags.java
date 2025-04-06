package net.silentchaos512.powerscale.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.silentchaos512.powerscale.PowerScale;

public class PsTags {
    public static final class EntityTypes {
        public static final TagKey<EntityType<?>> BLIGHT_EXEMPT = mod("blight_exempt");
        public static final TagKey<EntityType<?>> BLIGHTS_MAKE_GIANT = mod("blights/make_giant");
        public static final TagKey<EntityType<?>> BLIGHTS_STRIKE_WITH_LIGHTNING = mod("blights/strike_with_lightning");
        public static final TagKey<EntityType<?>> DIFFICULTY_EXEMPT = mod("difficulty_exempt");

        private static TagKey<EntityType<?>> mod(String path) {
            return TagKey.create(Registries.ENTITY_TYPE, PowerScale.getId(path));
        }
    }

    public static final class Items {
        public static final TagKey<Item> ALCHEMY_BREWS = mod("alchemy_brews");
        public static final TagKey<Item> ALCHEMY_FUELS = mod("alchemy_fuels");
        public static final TagKey<Item> CRYSTALS = mod("crystals");
        public static final TagKey<Item> FLASK_GEMS = mod("flask_gems");
        public static final TagKey<Item> POWER_LEVEL_DETECTORS = mod("power_level_detectors");

        private static TagKey<Item> mod(String path) {
            return TagKey.create(Registries.ITEM, PowerScale.getId(path));
        }
    }
}
