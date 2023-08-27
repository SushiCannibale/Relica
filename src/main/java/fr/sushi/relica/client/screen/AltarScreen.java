package fr.sushi.relica.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.sushi.relica.Relica;
import fr.sushi.relica.inventory.AltarMenu;
import fr.sushi.relica.block.entity.AltarBlockEntity;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class AltarScreen extends AbstractContainerScreen<AltarMenu> {
    public static final ResourceLocation ALTAR_BG = new ResourceLocation(Relica.MODID, "textures/gui/container/altar.png");
    private Rect2i fuelBarArea;
    public AltarScreen(AltarMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 186;
        this.imageHeight = 186;

        this.leftPos -= 5;
        this.topPos -= 10;

        this.inventoryLabelY += 20;
        this.inventoryLabelX += 4;
    }

    @Override
    protected void init() {
        super.init();
        this.fuelBarArea = new Rect2i(this.leftPos + 129, this.topPos + 14, 16, 52);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        this.renderBackground(pPoseStack);
        RenderSystem.setShaderTexture(0, ALTAR_BG);
        int x = this.leftPos;
        int y = this.topPos;
        blit(pPoseStack, x, y, 0, 0, this.imageWidth, this.imageHeight);

        this.drawFuel(pPoseStack);
        this.drawFuelValue(pPoseStack, pMouseX, pMouseY);

//        fill(pPoseStack, this.fuelBarArea.getX(), this.fuelBarArea.getY(), this.fuelBarArea.getX() + this.fuelBarArea.getWidth(), this.fuelBarArea.getY() + this.fuelBarArea.getHeight(), 0xffff0000);
    }

    private void drawFuel(PoseStack pPoseStack) {
        int fuelLevel = this.menu.getData().get(0);
        int barHeight = Mth.clamp((52 * fuelLevel) / AltarBlockEntity.MAX_FUEL, 0, 52);

        if (barHeight > 0) {
            blit(pPoseStack, this.leftPos + 130, this.topPos + 66 - barHeight, 186, 52 - barHeight, 15, barHeight);
        }

//        fill(pPoseStack, dw, dh, dw + 2, dh + 2, 0xff00ff00);
    }

    private void drawFuelValue(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        if (this.fuelBarArea.contains(pMouseX, pMouseY)) {
            Component component = Component.translatable("altar.fuel_level").append(this.menu.getFuel() + " / " + AltarBlockEntity.MAX_FUEL);
            this.renderTooltip(pPoseStack, component, pMouseX, pMouseY);
        }
    }
}
