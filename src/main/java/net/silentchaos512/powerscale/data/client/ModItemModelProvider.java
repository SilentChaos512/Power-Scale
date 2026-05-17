package net.silentchaos512.powerscale.data.client;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.RangeSelectItemModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.silentchaos512.powerscale.client.renderer.properties.DifficultyRatioItemModelProperty;
import net.silentchaos512.powerscale.setup.PsItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;

public class ModItemModelProvider extends ItemModelGenerators {
    public ModItemModelProvider(ItemModelOutput itemModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
        super(itemModelOutput, modelOutput);
    }

    @Override
    public void run() {
        generateFlatItem(PsItems.ALCHEMY_POWDER.get(), ModelTemplates.FLAT_ITEM);
        generateFlatItem(PsItems.HEART_CRYSTAL.get(), ModelTemplates.FLAT_ITEM);
        generateFlatItem(PsItems.POWER_CRYSTAL.get(), ModelTemplates.FLAT_ITEM);
        generateFlatItem(PsItems.ARCHER_CRYSTAL.get(), ModelTemplates.FLAT_ITEM);
        generateFlatItem(PsItems.WING_CRYSTAL.get(), ModelTemplates.FLAT_ITEM);
        generateFlatItem(PsItems.CURSED_HEART.get(), ModelTemplates.FLAT_ITEM);
        generateFlatItem(PsItems.ENCHANTED_HEART.get(), ModelTemplates.FLAT_ITEM);
        generateFlatItem(PsItems.FLASK.get(), ModelTemplates.FLAT_ITEM);
        generateFlatItem(PsItems.WATER_FLASK.get(), ModelTemplates.FLAT_ITEM);
        generateFlatItem(PsItems.MELLOW_BREW.get(), ModelTemplates.FLAT_ITEM);
        generateFlatItem(PsItems.TORPID_BREW.get(), ModelTemplates.FLAT_ITEM);
        generateFlatItem(PsItems.PRETENTIOUS_BREW.get(), ModelTemplates.FLAT_ITEM);
        generateFlatItem(PsItems.HEALTH_BOOSTER_TONIC.get(), ModelTemplates.FLAT_ITEM);
        generateFlatItem(PsItems.POWER_BOOSTER_TONIC.get(), ModelTemplates.FLAT_ITEM);
        generateFlatItem(PsItems.ARROW_POWER_BOOSTER_TONIC.get(), ModelTemplates.FLAT_ITEM);
        generateFlatItem(PsItems.SPEED_BOOSTER_TONIC.get(), ModelTemplates.FLAT_ITEM);
        generateFlatItem(PsItems.ARDUOUS_BREW.get(), ModelTemplates.FLAT_ITEM);
        generateFlatItem(PsItems.LANGUID_BREW.get(), ModelTemplates.FLAT_ITEM);

        generateDifficultyMeterItem(PsItems.DIFFICULTY_METER.get());
    }

    public void generateDifficultyMeterItem(Item item) {
        List<RangeSelectItemModel.Entry> list = new ArrayList<>();
        ItemModel.Unbaked model0 = ItemModelUtils.plainModel(this.createFlatItemModel(item, "_0", ModelTemplates.FLAT_ITEM));
        list.add(ItemModelUtils.override(model0, 0.0F));

        int modelSteps = 14;
        float offset = 1f / (2f * modelSteps);
        for (int i = 1; i < modelSteps; i++) {
            ItemModel.Unbaked modelSub = ItemModelUtils.plainModel(
                    this.createFlatItemModel(item, String.format(Locale.ROOT, "_%d", i), ModelTemplates.FLAT_ITEM)
            );
            list.add(ItemModelUtils.override(modelSub, (float) i / (float) modelSteps - offset));
        }

        this.itemModelOutput
                .accept(
                        item,
                        ItemModelUtils.rangeSelect(DifficultyRatioItemModelProperty.INSTANCE, list)
                );
    }
}
