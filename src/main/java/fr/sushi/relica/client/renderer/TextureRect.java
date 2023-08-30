package fr.sushi.relica.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.MatrixUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.joml.*;

import java.util.Vector;

public class TextureRect {

    private Vector3f center;
    private Vector3f offset;
    private Vector3f scale;
    private UvMap uvMat;

    public TextureRect(Vector3f center, Vector3f offset, Vector3f scale, UvMap uv) {
        this.center = center;
        this.offset = offset;
        this.scale = scale;
        this.uvMat = uv;
    }

    public void center(PoseStack pPoseStack) {
        pPoseStack.translate(this.center.x, this.center.y, this.center.z);
    }

    public void draw(VertexConsumer consumer, Matrix4f pPose, int pPackedLight) {
        /* topleft; botomleft; bottomleft; topright */
        consumer.vertex(pPose, 0f * this.scale.x + this.offset.x, 0f * this.scale.y + offset.y, 0f * this.scale.z + this.offset.z)
                .color(255, 255, 255, 255)
                .uv(this.uvMat.u0, this.uvMat.v0)
                .uv2(pPackedLight)
                .endVertex();

        consumer.vertex(pPose, 0f * this.scale.x + this.offset.x, -0.25f * this.scale.y + offset.y, 0f * this.scale.z + this.offset.z)
                .color(255, 255, 255, 255)
                .uv(this.uvMat.u1, this.uvMat.v1)
                .uv2(pPackedLight)
                .endVertex();

        consumer.vertex(pPose, -0.75f * this.scale.x +this. offset.x, -0.25f * this.scale.y + offset.y, 0f * this.scale.z + this.offset.z)
                .color(255, 255, 255, 255)
                .uv(this.uvMat.u2, this.uvMat.v2)
                .uv2(pPackedLight)
                .endVertex();

        consumer.vertex(pPose, -0.75f * this.scale.x + this.offset.x, 0f * this.scale.y + offset.y, 0f * this.scale.z + this.offset.z)
                .color(255, 255, 255, 255)
                .uv(this.uvMat.u3, this.uvMat.v3)
                .uv2(pPackedLight)
                .endVertex();

    }
}
