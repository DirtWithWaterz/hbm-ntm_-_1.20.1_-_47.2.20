package com.hbm.nucleartech.screen;

import com.hbm.nucleartech.HBM;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BurnerPressScreen extends AbstractContainerScreen<BurnerPressMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(HBM.MOD_ID, "textures/gui/burner_press_gui.png");

    public BurnerPressScreen(BurnerPressMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        pGuiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        this.titleLabelX = (imageWidth / 2) - 33;

        renderProgressPressStamp(pGuiGraphics, x, y);
        renderFuelGauge(pGuiGraphics, x, y);
        renderHeatGauge(pGuiGraphics, x, y);
    }

    private void renderHeatGauge(GuiGraphics pGuiGraphics, int x, int y) {

        int gaugeIdx = menu.getHeatGaugeProgress();

        int offset = (gaugeIdx*18)+14;

        pGuiGraphics.blit(TEXTURE, x + 25, y + 16, 176, offset, 18, 18);
    }

    private void renderProgressPressStamp(GuiGraphics pGuiGraphics, int x, int y) {

//        if(menu.isCrafting())
        pGuiGraphics.blit(TEXTURE, x + 80, y + 35, 195, 0, 16, menu.getScaledPressProgress());
    }

    private void renderFuelGauge(GuiGraphics pGuiGraphics, int x, int y) {

        int filled = menu.getScaledFuelProgress();

        int yOffset = 14 - filled;

        pGuiGraphics.blit(TEXTURE, x + 27, y + 37 + yOffset, 176, yOffset, 14, filled);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {

        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
