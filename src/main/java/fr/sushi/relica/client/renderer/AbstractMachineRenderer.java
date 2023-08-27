package fr.sushi.relica.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fr.sushi.relica.Relica;
import fr.sushi.relica.block.entity.MachineBlockEntity;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public abstract class AbstractMachineRenderer<T extends MachineBlockEntity> implements BlockEntityRenderer<T> {
    protected final EntityRenderDispatcher entityRenderDispatcher;

    private static final ResourceLocation PROGRESS_BAR = new ResourceLocation(Relica.MODID, "textures/display/progress_bar.png");
//    public static final RenderType BAR = RenderType.text(PROGRESS_BAR);

    protected AbstractMachineRenderer(BlockEntityRendererProvider.Context pContext) {
        this.entityRenderDispatcher = pContext.getEntityRenderer();
        RenderSystem.setShaderTexture(0, PROGRESS_BAR);
    }

    protected void renderProgress(PoseStack pPoseStack, MultiBufferSource pBufferSource, int pProgress, int pPackedLight) {
        pPoseStack.pushPose();

        pPoseStack.translate(1D, 1D, 1D);
        pPoseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        GuiComponent.blit(pPoseStack, 0, 0, 0, 0, 10, 16);
        pPoseStack.popPose();
    }
}
