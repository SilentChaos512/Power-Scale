package net.silentchaos512.powerscale.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.setup.PsTags;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public ModEntityTypeTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider) {
        super(pOutput, pProvider, PowerScale.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(PsTags.EntityTypes.BLIGHT_EXEMPT)
                .add(
                        EntityType.WARDEN
                );
        tag(PsTags.EntityTypes.BLIGHTS_MAKE_GIANT)
                .add(
                        EntityType.ZOMBIE,
                        EntityType.DROWNED,
                        EntityType.HUSK
                );
        tag(PsTags.EntityTypes.BLIGHTS_STRIKE_WITH_LIGHTNING)
                .add(
                        EntityType.CREEPER
                );

        tag(PsTags.EntityTypes.DIFFICULTY_EXEMPT);
    }
}
