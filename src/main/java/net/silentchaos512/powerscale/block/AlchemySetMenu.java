package net.silentchaos512.powerscale.block;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;

public class AlchemySetMenu extends AbstractContainerMenu {
    public AlchemySetMenu(int containerId, Inventory playerInventory, AlchemySetBlockEntity container, ContainerData dataAccess) {
        super(PsMenuTypes.ALCHEMY_SET.get(), containerId);
    }
}
