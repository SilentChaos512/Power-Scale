package net.silentchaos512.powerscale.compat.jade;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.silentchaos512.powerscale.block.AlchemySetBlockEntity;
import net.silentchaos512.powerscale.setup.PsItems;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.StreamServerDataProvider;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.IElementHelper;

public class PsJadeAlchemySetProvider implements IBlockComponentProvider, StreamServerDataProvider<BlockAccessor, PsJadeAlchemySetProvider.Data> {
    static final PsJadeAlchemySetProvider INSTANCE = new PsJadeAlchemySetProvider();

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor level, IPluginConfig config) {
        Data data = decodeFromData(level).orElse(null);
        if (data == null) return;

        IElementHelper helper = IElementHelper.get();
        tooltip.add(helper.smallItem(new ItemStack(PsItems.ALCHEMY_POWDER.get())).message(null));
        tooltip.append(helper.text(IThemeHelper.get().info(data.fuel)).message(I18n.get("narration.powerscale.alchemy_set.fuel", data.fuel)));
        if (data.time > 0) {
            tooltip.append(helper.spacer(5, 0));
            tooltip.append(helper.smallItem(new ItemStack(Items.CLOCK)).message(" "));
            tooltip.append(IThemeHelper.get().seconds(data.time, level.tickRate()));
        }
    }

    @Override
    public @Nullable Data streamData(BlockAccessor level) {
        var blockEntity = (AlchemySetBlockEntity) level.getBlockEntity();
        return new Data(blockEntity.getFuel(), blockEntity.getBrewTime());
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, Data> streamCodec() {
        return Data.STREAM_CODEC.cast();
    }

    @Override
    public ResourceLocation getUid() {
        return PsJadePlugin.ALCHEMY_SET;
    }

    public record Data(int fuel, int time) {
        public static final StreamCodec<ByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, Data::fuel,
                ByteBufCodecs.VAR_INT, Data::time,
                Data::new
        );
    }
}
