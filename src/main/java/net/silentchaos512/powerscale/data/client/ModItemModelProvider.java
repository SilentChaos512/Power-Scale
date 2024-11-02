package net.silentchaos512.powerscale.data.client;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.silentchaos512.lib.util.NameUtils;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.setup.PsItems;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, PowerScale.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        var itemGenerated = getExistingFile(ResourceLocation.withDefaultNamespace("item/generated"));

        builder(PsItems.HEART_CRYSTAL, itemGenerated);
        builder(PsItems.POWER_CRYSTAL, itemGenerated);
    }

    private ItemModelBuilder builder(ItemLike item) {
        return getBuilder(NameUtils.fromItem(item).getPath());
    }

    private ItemModelBuilder builder(ItemLike item, ModelFile parent) {
        String name = NameUtils.fromItem(item).getPath();
        return builder(item, parent, "item/" + name);
    }

    private ItemModelBuilder builder(ItemLike item, ModelFile parent, String texture) {
        return getBuilder(NameUtils.fromItem(item).getPath()).parent(parent).texture("layer0", texture);
    }
}
