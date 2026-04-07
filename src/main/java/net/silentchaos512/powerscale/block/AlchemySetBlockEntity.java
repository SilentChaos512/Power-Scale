package net.silentchaos512.powerscale.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.silentchaos512.powerscale.crafting.recipe.AlchemyRecipe;
import net.silentchaos512.powerscale.crafting.recipe.AlchemyRecipeInput;
import net.silentchaos512.powerscale.setup.PsBlockEntityTypes;
import net.silentchaos512.powerscale.setup.PsCrafting;
import net.silentchaos512.powerscale.setup.PsTags;

import javax.annotation.Nullable;

public class AlchemySetBlockEntity extends BaseContainerBlockEntity {
    static final int FLASK_SLOT = 0;
    static final int INGREDIENT_SLOT = 1;
    static final int FUEL_SLOT = 2;

    private NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    int brewTime;
    private Item ingredient = Items.AIR;
    int fuel;
    private boolean lastHasFlask = false;

    private final RecipeManager.CachedCheck<AlchemyRecipeInput, AlchemyRecipe> quickCheck;

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
        this.quickCheck = RecipeManager.createCheck(PsCrafting.ALCHEMY_TYPE.get());
    }

    public int getFuel() {
        return fuel;
    }

    public int getBrewTime() {
        return brewTime;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AlchemySetBlockEntity blockEntity) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        var fuelStack = blockEntity.items.get(FUEL_SLOT);
        if (blockEntity.fuel <= 0 && fuelStack.is(PsTags.Items.ALCHEMY_FUELS)) {
            blockEntity.fuel = 10;
            fuelStack.shrink(1);
            setChanged(level, pos, state);
        }

        var recipe = blockEntity.getRecipe(serverLevel);
        var isBrewable = recipe != null;
        var isBrewing = blockEntity.brewTime > 0;
        var ingredientStack = blockEntity.items.get(INGREDIENT_SLOT);

        if (isBrewing) {
            blockEntity.brewTime--;
            var isDone = blockEntity.brewTime == 0;
            if (isDone && isBrewable) {
                doBrew(recipe, level, pos, blockEntity.items);
            } else if (!isBrewable && !ingredientStack.is(blockEntity.ingredient)) {
                blockEntity.brewTime = 0;
            }

            setChanged(level, pos, state);
        } else if (isBrewable && blockEntity.fuel > 0) {
            blockEntity.fuel--;
            blockEntity.brewTime = 400;
            blockEntity.ingredient = ingredientStack.getItem();
            setChanged(level, pos, state);
        }

        var hasFlask = !blockEntity.items.get(FLASK_SLOT).isEmpty();
        if (hasFlask != blockEntity.lastHasFlask) {
            blockEntity.lastHasFlask = hasFlask;
            var newState = state;
            if (!(state.getBlock() instanceof AlchemySetBlock)) {
                return;
            }

            newState = newState.setValue(AlchemySetBlock.HAS_FLASK, hasFlask);
            level.setBlock(pos, newState, 2);
        }
    }

    @Nullable
    private AlchemyRecipe getRecipe(ServerLevel levelIn) {
        var flask = this.items.get(FLASK_SLOT);
        if (flask.isEmpty()) return null;

        var ingredient = this.items.get(INGREDIENT_SLOT);
        if (ingredient.isEmpty()) return null;

        var recipe = this.quickCheck.getRecipeFor(new AlchemyRecipeInput(flask, ingredient), levelIn);
        return recipe.map(RecipeHolder::value).orElse(null);
    }

    private static void doBrew(AlchemyRecipe recipe, Level level, BlockPos pos, NonNullList<ItemStack> items) {
        var flask = items.get(FLASK_SLOT);
        var ingredient = items.get(INGREDIENT_SLOT);

        items.set(0, recipe.assemble(new AlchemyRecipeInput(flask, ingredient), level.registryAccess()));

        ItemStack remainingItem = ingredient.getCraftingRemainder();
        if (!remainingItem.isEmpty()) {
            ingredient.shrink(1);
            if (ingredient.isEmpty()) {
                ingredient = remainingItem;
            } else {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), remainingItem);
            }
        } else {
            ingredient.shrink(1);
        }

        items.set(1, ingredient);
        level.levelEvent(1035, pos, 0);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, this.items);
        this.brewTime = input.getShortOr("BrewTime", (short) 0);
        if (this.brewTime > 0) {
            this.ingredient = this.items.get(INGREDIENT_SLOT).getItem();
        }

        this.fuel = input.getByteOr("Fuel", (byte) 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putShort("BrewTime", (short) this.brewTime);
        ContainerHelper.saveAllItems(output, this.items);
        output.putByte("Fuel", (byte) this.fuel);
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

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == FLASK_SLOT) {
            return stack.is(PsTags.Items.ALCHEMY_BREWS);
        } else if (slot == FUEL_SLOT) {
            return stack.is(PsTags.Items.ALCHEMY_FUELS);
        }
        return super.canPlaceItem(slot, stack);
    }
}
