package fr.sushi.relica.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import fr.sushi.relica.Relica;
import fr.sushi.relica.block.entity.MachineBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.joml.Quaterniond;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class AbstractMachineRenderer<T extends MachineBlockEntity> implements BlockEntityRenderer<T> {
    protected final EntityRenderDispatcher entityRenderDispatcher;

    public static final ResourceLocation PROGRESS_BAR = new ResourceLocation(Relica.MODID, "textures/display/progress_bar.png");
    private static final int BAR_LENGTH = 36;
    private final RenderType renderType;

    protected AbstractMachineRenderer(BlockEntityRendererProvider.Context pContext) {
        this.entityRenderDispatcher = pContext.getEntityRenderer();
        this.renderType = RenderType.text(PROGRESS_BAR);
    }

    protected void renderProgress(PoseStack pPoseStack, MultiBufferSource pBufferSource, int pProgress, int pPackedLight) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, PROGRESS_BAR);

        Matrix4f matrix4f = pPoseStack.last().pose();

        Quaternionf quat = this.entityRenderDispatcher.cameraOrientation();
        float halfWidth = 0.75f;

        pPoseStack.translate(0.5f, 1.5f, 0.5f);
        pPoseStack.mulPose(quat);

        VertexConsumer consumer = pBufferSource.getBuffer(this.renderType);
        consumer.vertex(matrix4f, 0f, 0f, 0f).color(255, 255, 255, 255).uv(0f, 0f).uv2(pPackedLight).endVertex();
        consumer.vertex(matrix4f, 0f, -0.25f, 0f).color(255, 255, 255, 255).uv(0f, 0.25f).uv2(pPackedLight).endVertex();
        consumer.vertex(matrix4f, -0.75f, -0.25f, 0f).color(255, 255, 255, 255).uv(0.75f, 0.25f).uv2(pPackedLight).endVertex();
        consumer.vertex(matrix4f, -0.75f, 0f, 0f).color(255, 255, 255, 255).uv(0.75f, 0f).uv2(pPackedLight).endVertex();

//        consumer.vertex(matrix4f, halfWidth, 1f - 0.5f, 0.0F).color(255, 255, 255, 255).uv(0f, 0.25f).uv2(pPackedLight).endVertex();
//        consumer.vertex(matrix4f, halfWidth - 2f, 1f - 0.5f, 0.0F).color(255, 255, 255, 255).uv(0.75f, 0.25f).uv2(pPackedLight).endVertex();
//        consumer.vertex(matrix4f, halfWidth - 2f, 1f, 0.0F).color(255, 255, 255, 255).uv(0.75f, 0f).uv2(pPackedLight).endVertex();
//        consumer.vertex(matrix4f, halfWidth, 1f, 0.0F).color(255, 255, 255, 255).uv(0f, 0f).uv2(pPackedLight).endVertex();

//        VertexConsumer consumer1 = pBufferSource.getBuffer(this.renderType);
//        consumer1.vertex(matrix4f, halfWidth + 6/64f, 1f - 0.5f + 6/64f, -0.01F).color(255, 255, 255, 255).uv(6/64f, 0.25f - 6/64f).uv2(pPackedLight).endVertex();
//        consumer1.vertex(matrix4f, halfWidth - 2f + 6/64f, 1f - 0.5f + 6/64f, -0.01F).color(255, 255, 255, 255).uv(0.75f - 6/64f, 0.25f - 6/64f).uv2(pPackedLight).endVertex();
//        consumer1.vertex(matrix4f, halfWidth - 2f + 6/64f, 1f - 6/64f, -0.01F).color(255, 255, 255, 255).uv(1f - 6/64f, 6/64f).uv2(pPackedLight).endVertex();
//        consumer1.vertex(matrix4f, halfWidth + 6/64f, 1f - 6/64f, -0.01F).color(255, 255, 255, 255).uv(6/64f, 6/64f).uv2(pPackedLight).endVertex();

//        pPoseStack.pushPose();
//
//        pPoseStack.mulPose(Axis.YP.rotationDegrees(90f));
//
//        pPoseStack.popPose();

//        Tesselator tes = Tesselator.getInstance();
//        BufferBuilder builder = tes.getBuilder();
//
//        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//        builder.vertex(matrix4f, 0f, 1f, -0.01f).uv(0f, 1f).endVertex();
//        builder.vertex(matrix4f, 64f, 64f, -0.01f).uv(1f, 1f).endVertex();
//        builder.vertex(matrix4f, 64f, 0f, -0.01f).uv(1f, 0f).endVertex();
//        builder.vertex(matrix4f, 0f, 0f, -0.01f).uv(0f, 0f).endVertex();
//
//        tes.end();
    }

    /* From github.ToroHealth */
//    private static void drawBar(Matrix4f matrix4f, double x, double y, float width, float percent,
//                                int color, int zOffset, boolean inWorld) {
//        float c = 0.00390625F;
//        int u = 0;
//        int v = 6 * 5 * 2 + 5;
//        int uw = MathHelper.ceil(92 * percent);
//        int vh = 5;
//
//        double size = percent * width;
//        double h = inWorld ? 4 : 6;
//
//        float r = (color >> 16 & 255) / 255.0F;
//        float g = (color >> 8 & 255) / 255.0F;
//        float b = (color & 255) / 255.0F;
//
//        RenderSystem.setShaderColor(r, g, b, 1);
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShaderTexture(0, GUI_BARS_TEXTURES);
//        RenderSystem.enableBlend();
//
//        float half = width / 2;
//
//        float zOffsetAmount = inWorld ? -0.1F : 0.1F;
//
//        Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder buffer = tessellator.getBuffer();
//        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
//        buffer.vertex(matrix4f, (float) (-half + x), (float) y, zOffset * zOffsetAmount).texture(u * c, v * c).next();
//        buffer.vertex(matrix4f, (float) (-half + x), (float) (h + y), zOffset * zOffsetAmount).texture(u * c, (v + vh) * c).next();
//        buffer.vertex(matrix4f, (float) (-half + size + x), (float) (h + y), zOffset * zOffsetAmount).texture((u + uw) * c, (v + vh) * c).next();
//        buffer.vertex(matrix4f, (float) (-half + size + x), (float) y, zOffset * zOffsetAmount).texture(((u + uw) * c), v * c).next();
//        tessellator.draw();
//    }
}
