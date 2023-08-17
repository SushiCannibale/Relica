package fr.sushi.relica.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import fr.sushi.relica.block.AltarBlock;
import fr.sushi.relica.entity.tileentity.AltarBlockEntity;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.EnchantTableRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AltarRenderer implements BlockEntityRenderer<AltarBlockEntity> {
    private Container container;
    private final ItemRenderer itemRenderer;
    private final BlockRenderDispatcher blockRender;

    public AltarRenderer(BlockEntityRendererProvider.Context pContext) {
        this.itemRenderer = pContext.getItemRenderer();
        this.blockRender = pContext.getBlockRenderDispatcher();
    }

    private void updateContainer(Container container) {
        this.container = container;
    }

    private void renderCandles(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        ItemStack itemstack = this.container.getItem(0);
        BakedModel model = this.blockRender.getBlockModel(Block.byItem(itemstack.getItem()).defaultBlockState());
        pPoseStack.pushPose();
        pPoseStack.translate(0.0f, 1.0f, 0.0f);
        this.blockRender.getModelRenderer().renderModel(pPoseStack.last(), pBuffer.getBuffer(RenderType.solid()), null, model, 1.0f, 1.0f, 1.0f, pPackedLight, pPackedOverlay);
        pPoseStack.popPose();
    }

    public void render(AltarBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        this.updateContainer(pBlockEntity);
        ItemStack stack = pBlockEntity.getItem(0);
        this.renderCandles(pPoseStack, pBufferSource, pPackedLight, pPackedOverlay);

//        pPoseStack.pushPose();
//        pPoseStack.translate(0.5F, 1.025F, 0.5F);
//        float f = blockstate.getValue(AltarBlock.FACING).getClockWise().toYRot();
//        pPoseStack.mulPose(Axis.YP.rotationDegrees(-f));
//        pPoseStack.mulPose(Axis.ZP.rotationDegrees(67.5F));
//
//        pPoseStack.translate(0.0F, -0.1F, 0.0F);
//
//        this.bookModel.setupAnim(0.0F, 0.1F, 0.9F, 1.2F);
//        VertexConsumer vertexconsumer = EnchantTableRenderer.BOOK_LOCATION.buffer(pBufferSource, RenderType::entitySolid);
//        this.bookModel.render(pPoseStack, vertexconsumer, pPackedLight, pPackedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
//        pPoseStack.popPose();
    }
}
