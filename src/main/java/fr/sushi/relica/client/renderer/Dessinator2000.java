package fr.sushi.relica.client.renderer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.resources.ResourceLocation;

public class Dessinator2000 {

    private ResourceLocation texture;

    public Dessinator2000(ResourceLocation texture) {
        this.texture = texture;
    }

    public static void drawRect(VertexConsumer consumer, float x, float y, float z, int u, int v, int packedLight) {
        consumer.vertex()
    }
}
