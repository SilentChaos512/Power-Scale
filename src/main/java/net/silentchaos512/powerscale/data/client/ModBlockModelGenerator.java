package net.silentchaos512.powerscale.data.client;

import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.resources.Identifier;
import net.silentchaos512.lib.data.client.LibBlockModelGenerators;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.block.AlchemySetBlock;
import net.silentchaos512.powerscale.setup.PsBlocks;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ModBlockModelGenerator extends LibBlockModelGenerators {
    public ModBlockModelGenerator(Consumer<BlockModelDefinitionGenerator> blockStateOutput, ItemModelOutput itemModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
        super(blockStateOutput, itemModelOutput, modelOutput);
    }

    @Override
    public void run() {
        alchemySet();
    }

    private void alchemySet() {
        var withoutFlask = plainVariant(PowerScale.getId("block/alchemy_set"));
        var withFlask = plainVariant(PowerScale.getId("block/alchemy_set_flask"));
        this.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(PsBlocks.ALCHEMY_SET.get())
                        .with(createBooleanModelDispatch(AlchemySetBlock.HAS_FLASK, withFlask, withoutFlask))
                        .with(ROTATION_HORIZONTAL_FACING)
        );
    }
}
