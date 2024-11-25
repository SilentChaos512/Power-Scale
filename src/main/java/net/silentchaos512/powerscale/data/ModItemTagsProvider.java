package net.silentchaos512.powerscale.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.setup.PsTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, PowerScale.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(PsTags.Items.FLASK_GEMS)
                .addTag(Tags.Items.GEMS_AMETHYST)
                .addTag(Tags.Items.GEMS_EMERALD)
                .addTag(Tags.Items.GEMS_QUARTZ);
    }
}
