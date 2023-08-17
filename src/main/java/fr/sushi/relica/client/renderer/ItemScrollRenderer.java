package fr.sushi.relica.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * This class handles the rendering process of the scroll when both held by the player
 * and on the Altar.

 * See <MapRenderer>
 */
@OnlyIn(Dist.CLIENT)
public class ItemScrollRenderer implements AutoCloseable {

    /**
     * Notes :
     * <param: int> pPackedLight : Le niveau d'intensité lumineuse pour calculer la couleur effective du pixel rendu à l'écran
     * - Vertex : Describe a point (2D or 3D)
     * - VertexConsumer :
     *      - <.vertex> permet de créer un sommet (4 sommets = un carré par exemple.)
     *          - les coordonnées x, y, z sont relatives à la matrice passée en 1er param
     *      - <.uv> permet de préciser les coordonnées des sommets données sur une texture (de 0 à 1)
     *      - <.uv2> est liée à l'intensité de la lumière projetée sur le polygone
     *      - <.color> permet de choisir quels channels de couleur seront utilisés
     */

    @Override
    public void close() throws Exception {
        // close the texture
    }

    public void render(PoseStack poseStack, MultiBufferSource bufferSource, boolean pActive, int pPackedLight) {
        // render the inside of the scroll

    }
}
