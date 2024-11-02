package net.silentchaos512.powerscale.setup;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.item.AttributeMutatorItem;

public class PsItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PowerScale.MOD_ID);

    public static final DeferredItem<AttributeMutatorItem> HEART_CRYSTAL = ITEMS.register(
            "heart_crystal",
            () -> new AttributeMutatorItem(new Item.Properties())
    );
    public static final DeferredItem<AttributeMutatorItem> POWER_CRYSTAL = ITEMS.register(
            "heart_crystal",
            () -> new AttributeMutatorItem(new Item.Properties())
    );
}
