package fr.sushi.relica.item;

import com.mojang.blaze3d.vertex.VertexConsumer;
import fr.sushi.relica.Relica;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class ItemScroll extends Item {
    public ItemScroll(Item.Properties properties) {
        super(properties);
        properties.stacksTo(1);
    }

    public static final ResourceLocation SCROLL_TEXTURE = new ResourceLocation(Relica.MODID, "textures/scroll/scroll_background");

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pLevel.isClientSide || !pIsSelected)
            return;

//        if (pLevel.isClientSide && pIsSelected) {
//            VertexConsumer vConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines());
//            vConsumer.vertex(0D, 0D, 0D).color(255, 0, 0, 255).endVertex();
//        }
    }

    /*
    * Display similaire à la map de base.
    *
    * - une classe ScrollRenderer qui propose les fonctions :
    *   - render (gère le rendu dans les 2 mains, et sur l'altar)
    *
    */
}
