package fr.sushi.relica.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fr.sushi.relica.Relica;
import fr.sushi.relica.block.AltarBlock;
import fr.sushi.relica.registry.ModBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(modid = Relica.MODID)
public class ForgeEvents {

    @SubscribeEvent
    public static void renderHand(RenderHandEvent event) {
//        PoseStack poseStack = event.getPoseStack();
//        VertexConsumer consumer = event.getMultiBufferSource().getBuffer(RenderType.LINES);
//
//        poseStack.pushPose();
//
//        consumer.vertex(0D, 0D, 0D).color(255, 0, 0, 255).endVertex();
//        consumer.vertex(0D, 1D, 0D).color(255, 0, 0, 255).endVertex();
//        consumer.vertex(0D, 0D, 1D).color(255, 0, 0, 255).endVertex();

//        consumer.vertex(0D, 1D, 1D).color(255, 0, 0, 255).endVertex();
//        consumer.vertex(0D, 1D, 1D).color(255, 0, 0, 255).endVertex();
//        consumer.vertex(0D, 1D, 1D).color(255, 0, 0, 255).endVertex();


//        poseStack.popPose();
    }

//    @SubscribeEvent
//    public static void onItemUseOn(PlayerInteractEvent.RightClickBlock event) {
//        Level level = event.getLevel();
//        BlockPos pos = event.getPos();
//        BlockState blockstate = level.getBlockState(pos);
//        Player player = event.getEntity();
//
//        if (blockstate.is(ModBlocks.ALTAR.get())) {
//            AltarBlock altar = (AltarBlock)blockstate.getBlock();
//            InteractionResult result = altar.rightClicked(level, blockstate, pos, player, event.getHand());
//            if (event.getItemStack().is(ItemTags.LECTERN_BOOKS)) {
//                event.setCancellationResult(result);
//            }
//        }
//    }
}
