package com.hbm.nucleartech.screen;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.util.FloatingLong;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShredderScreen extends AbstractContainerScreen<ShredderMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(HBM.MOD_ID, "textures/gui/shredder_gui.png");

    public ShredderScreen(ShredderMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - (imageHeight+56)) / 2;

        pGuiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight+56);

//        this.titleLabelX = (imageWidth / 2) - 33;
        this.titleLabelY = (imageHeight / 2) - 105;
        this.inventoryLabelY = ((imageHeight + 56) / 2) - 11;

        renderShredProgress(pGuiGraphics, x, y);
        renderPowerBar(pGuiGraphics, x, y);
        renderLeftBlade(pGuiGraphics, x, y);
        renderRightBlade(pGuiGraphics, x, y);
    }

    private void renderRightBlade(GuiGraphics pGuiGraphics, int x, int y) {

        int[] durability = menu.getBladeDurability((byte)1);

        int uOffset;
        int vOffset;

        if(durability[0] >= durability[1]) {

            // red
            uOffset = 196;
            vOffset = 37;
        }
        else if(durability[0] >= durability[1]/2) {

            // yellow
            uOffset = 196;
            vOffset = 19;
        }
        else if(durability[0] >= 0) {

            // white
            uOffset = 196;
            vOffset = 1;
        }
        else {

            return;
        }

        pGuiGraphics.blit(TEXTURE, x + 81, y + 72, uOffset, vOffset, 14, 15);
    }

    private void renderLeftBlade(GuiGraphics pGuiGraphics, int x, int y) {

        int[] durability = menu.getBladeDurability((byte)0);

        int uOffset;
        int vOffset;

        if(durability[0] >= durability[1]) {

            // red
            uOffset = 178;
            vOffset = 37;
        }
        else if(durability[0] >= durability[1]/2) {

            // yellow
            uOffset = 178;
            vOffset = 19;
        }
        else if(durability[0] >= 0) {

            // white
            uOffset = 178;
            vOffset = 1;
        }
        else {

            return;
        }

        pGuiGraphics.blit(TEXTURE, x + 45, y + 72, uOffset, vOffset, 14, 15);
    }

    private void renderShredProgress(GuiGraphics pGuiGraphics, int x, int y) {

        if(menu.isCrafting())
            pGuiGraphics.blit(TEXTURE, x + 64, y + 89, 177, 54, menu.getScaledShredderProgress(), 13);
    }

    private void renderPowerBar(GuiGraphics pGuiGraphics, int x, int y) {

        int filled = menu.getScaledEnergyProgress(); // 0..88

        // Destination: draw at the same x, y, but offset by how much is empty
        int yOffset = 18 + (88 - filled); // 18 is top of the empty bar
        int height = filled;              // how tall the filled part is

        // Source: take the bottom 'filled' pixels of the filler texture
        int srcY = 72 + (88 - filled);    // 72 is top of filler in texture

        pGuiGraphics.blit(TEXTURE, x + 8, y + yOffset, 176, srcY, 16, height);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {

        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);

        // check hover and show tooltip
        if (isMouseOverGuiArea(pMouseX, pMouseY, 8, 18-28, this.leftPos, this.topPos, 16, 88)) {
            String storedDisplay = menu.blockEntity.formatWattHoursStored() + "/" + menu.blockEntity.formatMaxWattHoursStored();

            List<Component> lines = new ArrayList<>();
            lines.add(Component.literal(storedDisplay));

            String discharge = menu.blockEntity.formatWattsDischarge();
            if (!Objects.equals(discharge, "0.0W")) {
                lines.add(Component.translatable("ui.hbm.power_draw", discharge));
            }

            // Multi-line tooltip: pass a List<Component>
            pGuiGraphics.renderComponentTooltip(Minecraft.getInstance().font, lines, pMouseX, pMouseY);
        }
    }

    /** helper: checks mouse position against GUI-relative rectangle */
    public static boolean isMouseOverGuiArea(int mouseX, int mouseY, int relX, int relY, int leftPos, int topPos, int width, int height) {
        int gx = leftPos + relX;
        int gy = topPos + relY;
        return mouseX >= gx && mouseY >= gy && mouseX < gx + width && mouseY < gy + height;
    }
}
