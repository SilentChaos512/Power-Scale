package net.silentchaos512.powerscale.compat.jade;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
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
import snownee.jade.api.ui.JadeUI;

public class PsJadeAlchemySetProvider implements StreamServerDataProvider<BlockAccessor, PsJadeAlchemySetProvider.Data> {
    static final PsJadeAlchemySetProvider INSTANCE = new PsJadeAlchemySetProvider();

    @Override
    public @Nullable Data streamData(BlockAccessor level) {
        var blockEntity = (AlchemySetBlockEntity) level.getBlockEntity();
        if (blockEntity == null) {
            return null;
        }
        return new Data(blockEntity.getFuel(), blockEntity.getBrewTime());
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, Data> streamCodec() {
        return Data.STREAM_CODEC.cast();
    }

    @Override
    public Identifier getUid() {
        return PsJadePlugin.ALCHEMY_SET;
    }

    public record Data(int fuel, int time) {
        public static final StreamCodec<ByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, Data::fuel,
                ByteBufCodecs.VAR_INT, Data::time,
                Data::new
        );
    }

    public static class Client implements IBlockComponentProvider {
        public static final Client INSTANCE = new Client();

        public Client() {
        }

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor level, IPluginConfig config) {
            Data data = PsJadeAlchemySetProvider.INSTANCE.decodeFromData(level).orElse(null);
            if (data == null) return;

            tooltip.add(JadeUI.smallItem(new ItemStack(PsItems.ALCHEMY_POWDER.get())));
            tooltip.append(JadeUI.text(IThemeHelper.get().info(data.fuel)).alignSelfCenter()/*.message(I18n.get("narration.powerscale.alchemy_set.fuel", data.fuel))*/);
            if (data.time > 0) {
                tooltip.append(JadeUI.spacer(5, 0));
                tooltip.append(JadeUI.smallItem(new ItemStack(Items.CLOCK)).alignSelfCenter());
                tooltip.append(IThemeHelper.get().seconds(data.time, level.tickRate()));
            }
        }

        @Override
        public Identifier getUid() {
            return PsJadePlugin.ALCHEMY_SET;
        }
    }
}
