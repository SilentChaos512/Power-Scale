package net.silentchaos512.powerscale.block;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.silentchaos512.powerscale.PowerScale;

public class AlchemySetScreen extends AbstractContainerScreen<AlchemySetMenu> {
    private static final ResourceLocation FUEL_LENGTH_SPRITE = PowerScale.getId("container/alchemy_set/fuel_length");
    private static final ResourceLocation BREW_PROGRESS_SPRITE = PowerScale.getId("container/alchemy_set/brew_progress");
    private static final ResourceLocation BUBBLES_SPRITE = PowerScale.getId("container/alchemy_set/bubbles");
    private static final ResourceLocation MAIN_TEXTURE = PowerScale.getId("textures/gui/container/alchemy_set.png");
    private static final int[] BUBBLE_LENGTHS = new int[]{29, 24, 20, 16, 11, 6, 0};

    public AlchemySetScreen(AlchemySetMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        pGuiGraphics.blit(MAIN_TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
        int fuel = this.menu.getFuel();
        int fuelLength = Mth.clamp((18 * fuel + 10 - 1) / 10, 0, 18);
        if (fuelLength > 0) {
            pGuiGraphics.blitSprite(FUEL_LENGTH_SPRITE, 18, 4, 0, 0, i + 60, j + 44, fuelLength, 4);
        }

        int brewingTicks = this.menu.getBrewingTicks();
        if (brewingTicks > 0) {
            int j1 = (int)(28.0F * (1.0F - (float)brewingTicks / 400.0F));
            if (j1 > 0) {
                pGuiGraphics.blitSprite(BREW_PROGRESS_SPRITE, 9, 28, 0, 0, i + 97, j + 16, 9, j1);
            }

            j1 = BUBBLE_LENGTHS[brewingTicks / 2 % 7];
            if (j1 > 0) {
                pGuiGraphics.blitSprite(BUBBLES_SPRITE, 12, 29, 0, 29 - j1, i + 63, j + 14 + 29 - j1, 12, j1);
            }
        }
    }
}
