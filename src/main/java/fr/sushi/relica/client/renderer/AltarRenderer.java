package fr.sushi.relica.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.sushi.relica.block.AltarBlock;
import fr.sushi.relica.block.entity.AltarBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class AltarRenderer implements BlockEntityRenderer<AltarBlockEntity> {
    private final ItemRenderer itemRenderer;
    private final BlockRenderDispatcher blockRender;

    public AltarRenderer(BlockEntityRendererProvider.Context pContext) {
        this.itemRenderer = pContext.getItemRenderer();
        this.blockRender = pContext.getBlockRenderDispatcher();
    }

    private void renderCandles(AltarBlockEntity pBlockEntity, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

    }

    public void render(AltarBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        Direction direction = pBlockEntity.getBlockState().getValue(AltarBlock.FACING);
        NonNullList<ItemStack> items = pBlockEntity.getItems();

        for (int i = 0; i < items.size(); i++) {
            ItemStack itemstack = items.get(i);

            pPoseStack.pushPose();
            boolean isBlock;


            if (i == 4) {
                // TODO Render Scroll on the altar
                isBlock = false;
                /* Candles : slotIndex = 0[2] */
            } else if (i % 2 == 0) {
                isBlock = true;

                pPoseStack.translate(0.5D, 1.0D, 0.5D);
                pPoseStack.mulPose(Axis.YP.rotationDegrees(180f - direction.toYRot()));

                /* X axis ⟂ direction     |     Z axis // direction  */
                switch (i) {
                    case 0 -> pPoseStack.translate(-0.1D, 0.0D, -0.25D);
                    case 2 -> pPoseStack.translate(-1.0D + 0.1D, 0.0D, -0.25D);
                    case 6 -> pPoseStack.translate(-0.1D, -0.25D, -1.0D + 0.15D);
                    case 8 -> pPoseStack.translate(-1.0D + 0.1D, -0.25D, -1.0D + 0.15D);
                }

            } else {
                isBlock = false;

                pPoseStack.translate(0.5f, 0.5f, 0.5f);
                pPoseStack.mulPose(Axis.YP.rotationDegrees(180f - direction.toYRot()));

                switch (i) {
                    case 1:
                        pPoseStack.translate(0.0f, 1.0f, 0.5f);
                        break;
                    case 3:
                        pPoseStack.translate(-0.5f, 1.0f, 0f);
                        break;
                    case 5:
                        pPoseStack.translate(0.5f, 1.0f, 0f);
                        break;
                    case 7:
                        pPoseStack.translate(0.0f, 1.0f, -0.5f);
                        break;
                }

                float bob = Mth.sin(pPartialTick / 10f) * 0.1f;
//                pPoseStack.translate(0.0D, bob, 0.0D);
                pPoseStack.mulPose(Axis.YP.rotation(pPartialTick / 20f));
                pPoseStack.scale(0.45f, 0.45f, 0.45f);
            }

            BakedModel model;

            if (isBlock) {
                model = this.blockRender.getBlockModel(Block.byItem(itemstack.getItem()).defaultBlockState());
                this.blockRender.getModelRenderer().renderModel(pPoseStack.last(), pBufferSource.getBuffer(RenderType.solid()), null, model, 1.0f, 1.0f, 1.0f, pPackedLight, pPackedOverlay);

            } else {
                model = this.itemRenderer.getModel(itemstack, pBlockEntity.getLevel(), null, 0);
                this.itemRenderer.render(itemstack, ItemDisplayContext.FIXED, false, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay, model);
            }

            pPoseStack.popPose();
        }
    }
}
