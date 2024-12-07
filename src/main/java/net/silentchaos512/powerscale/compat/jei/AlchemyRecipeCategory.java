package net.silentchaos512.powerscale.compat.jei;

import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.crafting.recipe.AlchemyRecipe;
import net.silentchaos512.powerscale.setup.PsBlocks;
import net.silentchaos512.powerscale.setup.PsTags;
import org.jetbrains.annotations.Nullable;

public class AlchemyRecipeCategory extends AbstractRecipeCategory<AlchemyRecipe> {
    private final IDrawable background;
    private final IDrawableAnimated arrow;
    private final IDrawableAnimated bubbles;
    private final IDrawableStatic fuel;

    public AlchemyRecipeCategory(IGuiHelper guiHelper) {
        super(
                PowerScaleJeiPlugin.ALCHEMY_RECIPE_TYPE,
                Component.translatable("gui.powerscale.category.alchemy"),
                guiHelper.createDrawableItemLike(PsBlocks.ALCHEMY_SET),
                114,
                61
        );

        this.background = createDrawable(guiHelper, "textures/jei/alchemy_set_background.png", 64, 60);
        this.arrow = guiHelper.createAnimatedDrawable(
                createDrawable(guiHelper, "textures/jei/alchemy_set_arrow.png", 7, 27),
                400,
                IDrawableAnimated.StartDirection.TOP,
                false
        );
        ITickTimer bubblesTickTimer = new BrewingBubblesTickTimer(guiHelper);
        this.bubbles = guiHelper.createAnimatedDrawable(
                createDrawable(guiHelper, "textures/jei/alchemy_set_bubbles.png", 11, 28),
                bubblesTickTimer,
                IDrawableAnimated.StartDirection.BOTTOM
        );
        this.fuel = createDrawable(guiHelper, "textures/jei/alchemy_set_fuel.png", 18, 4);
    }

    private static IDrawableStatic createDrawable(IGuiHelper guiHelper, String texturePath, int width, int height) {
        return guiHelper.drawableBuilder(PowerScale.getId(texturePath), 0, 0, width, height)
                .setTextureSize(width, height)
                .build();
    }

    @Override
    public void draw(AlchemyRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics, 0, 1);
        this.fuel.draw(guiGraphics, 5, 30);
        this.bubbles.draw(guiGraphics, 9, 1);
        this.arrow.draw(guiGraphics, 43, 3);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AlchemyRecipe recipe, IFocusGroup focusGroup) {
        builder.addInputSlot(1, 37).addIngredients(Ingredient.of(PsTags.Items.ALCHEMY_FUELS));
        builder.addInputSlot(24, 44).addIngredients(recipe.flask());
        builder.addInputSlot(24, 3).addIngredients(recipe.ingredient());
        builder.addOutputSlot(81, 3).addItemStack(recipe.result()).setStandardSlotBackground();
    }

    private static class BrewingBubblesTickTimer implements ITickTimer {
        private static final int[] BUBBLE_LENGTHS = new int[]{29, 23, 18, 13, 9, 5, 0};
        private final ITickTimer internalTimer;

        public BrewingBubblesTickTimer(IGuiHelper guiHelper) {
            this.internalTimer = guiHelper.createTickTimer(14, BUBBLE_LENGTHS.length - 1, false);
        }

        public int getValue() {
            int timerValue = this.internalTimer.getValue();
            return BUBBLE_LENGTHS[timerValue];
        }

        public int getMaxValue() {
            return BUBBLE_LENGTHS[0];
        }
    }
}
