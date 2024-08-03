package it.hurts.octostudios.cardiac.common.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.hurts.octostudios.cardiac.common.Cardiac;
import it.hurts.octostudios.cardiac.common.entities.LifeOrb;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class LifeOrbRenderer extends EntityRenderer<LifeOrb> {
    public LifeOrbRenderer(EntityRendererProvider.Context context) {
        super(context);

        this.shadowRadius = 0F;
        this.shadowStrength = 0.75F;
    }

    @Override
    protected int getBlockLightLevel(LifeOrb entity, BlockPos pos) {
        return Mth.clamp(super.getBlockLightLevel(entity, pos) + 7, 0, 15);
    }

    @Override
    public void render(LifeOrb entity, float yaw, float pitch, PoseStack poseStack, MultiBufferSource buffer, int light) {
        this.shadowRadius = 0.035F + (entity.getStage() * 0.025F);

        poseStack.pushPose();

        VertexConsumer consumer = buffer.getBuffer(RenderType.itemEntityTranslucentCull(getTextureLocation(entity)));
        PoseStack.Pose pose = poseStack.last();

        float scale = (float) (0.4F + Math.sin(entity.tickCount * 0.1F) * 0.05F);

        poseStack.scale(scale, scale, scale);

        poseStack.translate(0.0F, 0.1F + (entity.getStage() * 0.05F), 0.0F);

        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());

        int alpha = (int) Math.min(255, 255 * (0.75F + Math.sin(entity.tickCount * 0.25F) * 0.1F));

        vertex(consumer, pose, -0.5F, -0.5F, alpha, 0, 1);
        vertex(consumer, pose, 0.5F, -0.5F, alpha, 1, 1);
        vertex(consumer, pose, 0.5F, 0.5F, alpha, 1, 0);
        vertex(consumer, pose, -0.5F, 0.5F, alpha, 0, 0);

        poseStack.popPose();

        super.render(entity, yaw, pitch, poseStack, buffer, light);
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, int alpha, float u, float v) {
        consumer.addVertex(pose, x, y, 0F).setColor(255, 255, 255, alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 1F, 0F);
    }

    @Override
    public ResourceLocation getTextureLocation(LifeOrb entity) {
        return ResourceLocation.fromNamespaceAndPath(Cardiac.MODID, "textures/entity/life_orb_" + entity.getStage() + ".png");
    }
}