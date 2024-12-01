package net.silentchaos512.powerscale.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.silentchaos512.powerscale.setup.PsBlockEntityTypes;
import net.silentchaos512.powerscale.setup.PsTags;

public class AlchemySetBlockEntity extends BaseContainerBlockEntity {
    // Items: 0 = flask, 1 = ingredient, 2 = fuel
    private NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    int brewTime;
    private Item ingredient;
    int fuel;

    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int pIndex) {
            return switch (pIndex) {
                case 0 -> AlchemySetBlockEntity.this.brewTime;
                case 1 -> AlchemySetBlockEntity.this.fuel;
                default -> 0;
            };
        }

        @Override
        public void set(int pIndex, int pValue) {
            switch (pIndex) {
                case 0:
                    AlchemySetBlockEntity.this.brewTime = pValue;
                    break;
                case 1:
                    AlchemySetBlockEntity.this.fuel = pValue;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public AlchemySetBlockEntity(BlockPos pPos, BlockState pState) {
        super(PsBlockEntityTypes.ALCHEMY_SET.get(), pPos, pState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AlchemySetBlockEntity blockEntity) {
        var fuelStack = blockEntity.items.get(2);
        if (blockEntity.fuel <= 0 && fuelStack.is(PsTags.Items.ALCHEMY_FUEL)) {
            blockEntity.fuel = 10;
            fuelStack.shrink(1);
            setChanged(level, pos, state);
        }

        var isBrewable = isBrewable(level.potion)
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.powerscale.alchemy_set");
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> pItems) {
        this.items = pItems;
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new AlchemySetMenu(pContainerId, pInventory, this, this.dataAccess);
    }
}
