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
        withExistingParent("alchemy_set", modLoc("block/alchemy_set"));

        var itemGenerated = getExistingFile(ResourceLocation.withDefaultNamespace("item/generated"));

        builder(PsItems.ALCHEMY_POWDER, itemGenerated);
        builder(PsItems.HEART_CRYSTAL, itemGenerated);
        builder(PsItems.POWER_CRYSTAL, itemGenerated);
        builder(PsItems.ARCHER_CRYSTAL, itemGenerated);
        builder(PsItems.WING_CRYSTAL, itemGenerated);
        builder(PsItems.CURSED_HEART, itemGenerated);
        builder(PsItems.ENCHANTED_HEART, itemGenerated);
        builder(PsItems.FLASK, itemGenerated);
        builder(PsItems.WATER_FLASK, itemGenerated);
        builder(PsItems.MELLOW_BREW, itemGenerated);
        builder(PsItems.TORPID_BREW, itemGenerated);
        builder(PsItems.PRETENTIOUS_BREW, itemGenerated);
        builder(PsItems.HEALTH_BOOSTER_TONIC, itemGenerated);
        builder(PsItems.POWER_BOOSTER_TONIC, itemGenerated);
        builder(PsItems.ARROW_POWER_BOOSTER_TONIC, itemGenerated);
        builder(PsItems.SPEED_BOOSTER_TONIC, itemGenerated);
        builder(PsItems.ARDUOUS_BREW, itemGenerated);
        builder(PsItems.LANGUID_BREW, itemGenerated);


        for (int i = 0; i < 14; ++i) {
            var path = "difficulty_meter_" + i;
            getBuilder(path).parent(itemGenerated).texture("layer0", "item/" + path);
        }
        getBuilder("difficulty_meter")
                .parent(itemGenerated)
                .override().predicate(PowerScale.getId("difficulty"), 0f).model(getExistingFile(PowerScale.getId("difficulty_meter_0"))).end()
                .override().predicate(PowerScale.getId("difficulty"), 1f / 14f).model(getExistingFile(PowerScale.getId("difficulty_meter_1"))).end()
                .override().predicate(PowerScale.getId("difficulty"), 2f / 14f).model(getExistingFile(PowerScale.getId("difficulty_meter_2"))).end()
                .override().predicate(PowerScale.getId("difficulty"), 3f / 14f).model(getExistingFile(PowerScale.getId("difficulty_meter_3"))).end()
                .override().predicate(PowerScale.getId("difficulty"), 4f / 14f).model(getExistingFile(PowerScale.getId("difficulty_meter_4"))).end()
                .override().predicate(PowerScale.getId("difficulty"), 5f / 14f).model(getExistingFile(PowerScale.getId("difficulty_meter_5"))).end()
                .override().predicate(PowerScale.getId("difficulty"), 6f / 14f).model(getExistingFile(PowerScale.getId("difficulty_meter_6"))).end()
                .override().predicate(PowerScale.getId("difficulty"), 7f / 14f).model(getExistingFile(PowerScale.getId("difficulty_meter_7"))).end()
                .override().predicate(PowerScale.getId("difficulty"), 8f / 14f).model(getExistingFile(PowerScale.getId("difficulty_meter_8"))).end()
                .override().predicate(PowerScale.getId("difficulty"), 9f / 14f).model(getExistingFile(PowerScale.getId("difficulty_meter_9"))).end()
                .override().predicate(PowerScale.getId("difficulty"), 10f / 14f).model(getExistingFile(PowerScale.getId("difficulty_meter_10"))).end()
                .override().predicate(PowerScale.getId("difficulty"), 11f / 14f).model(getExistingFile(PowerScale.getId("difficulty_meter_11"))).end()
                .override().predicate(PowerScale.getId("difficulty"), 12f / 14f).model(getExistingFile(PowerScale.getId("difficulty_meter_12"))).end()
                .override().predicate(PowerScale.getId("difficulty"), 13f / 14f).model(getExistingFile(PowerScale.getId("difficulty_meter_13"))).end()
                .texture("layer0", "item/difficulty_meter_0");
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
