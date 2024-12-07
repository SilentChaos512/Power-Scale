package net.silentchaos512.powerscale.block;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.silentchaos512.lib.util.InventoryUtils;
import net.silentchaos512.powerscale.setup.PsMenuTypes;
import net.silentchaos512.powerscale.setup.PsTags;

public class AlchemySetMenu extends AbstractContainerMenu {
    private final Container inventory;
    final ContainerData dataAccess;

    public AlchemySetMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId, playerInventory, new SimpleContainer(3), new SimpleContainerData(2));
    }

    public AlchemySetMenu(int containerId, Inventory playerInventory, Container container, ContainerData dataAccess) {
        super(PsMenuTypes.ALCHEMY_SET.get(), containerId);
        this.inventory = container;
        this.dataAccess = dataAccess;

        checkContainerSize(this.inventory, 3);
        checkContainerDataCount(this.dataAccess, 2);

        addSlot(new BrewSlot(this.inventory, AlchemySetBlockEntity.FLASK_SLOT, 79, 58));
        addSlot(new Slot(this.inventory, AlchemySetBlockEntity.INGREDIENT_SLOT, 79, 17));
        addSlot(new FuelSlot(this.inventory, AlchemySetBlockEntity.FUEL_SLOT, 17, 17));

        this.addDataSlots(this.dataAccess);

        InventoryUtils.createPlayerSlots(playerInventory, 8, 84).forEach(this::addSlot);
    }

    public int getFuel() {
        return this.dataAccess.get(1);
    }

    public int getBrewingTicks() {
        return this.dataAccess.get(0);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return inventory.stillValid(pPlayer);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            stack = itemstack1.copy();

            final int inventorySize = 3;
            final int playerInventoryEnd = inventorySize + 27;
            final int playerHotbarEnd = playerInventoryEnd + 9;

            if (index < 0 || index > 2) {
                if (stack.is(PsTags.Items.ALCHEMY_FUELS)) {
                    if (this.moveItemStackTo(itemstack1, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (stack.is(PsTags.Items.ALCHEMY_BREWS)) {
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else {
                if (!this.moveItemStackTo(itemstack1, inventorySize, playerInventoryEnd, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, stack);
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }

        return stack;
    }

    static class FuelSlot extends Slot {
        public FuelSlot(Container pContainer, int pSlot, int pX, int pY) {
            super(pContainer, pSlot, pX, pY);
        }

        @Override
        public boolean mayPlace(ItemStack pStack) {
            return pStack.is(PsTags.Items.ALCHEMY_FUELS);
        }
    }

    static class BrewSlot extends Slot {
        public BrewSlot(Container pContainer, int pSlot, int pX, int pY) {
            super(pContainer, pSlot, pX, pY);
        }

        @Override
        public boolean mayPlace(ItemStack pStack) {
            return pStack.is(PsTags.Items.ALCHEMY_BREWS);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }
}
