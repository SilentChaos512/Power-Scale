package net.silentchaos512.powerscale.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.silentchaos512.lib.event.ClientTicks;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.setup.PsAttachmentTypes;
import org.joml.Quaternionf;

@EventBusSubscriber(value = Dist.CLIENT, modid = PowerScale.MOD_ID)
public class BlightRenderHandler {
    private static final float FIRE_SCALE = 1.8F;
    private static final ResourceLocation TEXTURE_NORMAL = PowerScale.getId("textures/entity/blightfire.png");
    private static final ResourceLocation TEXTURE_UNCOLORED = PowerScale.getId("textures/entity/blightfire_grayscale.png");
    private static RenderType RENDER_TYPE_NORMAL;
    private static RenderType RENDER_TYPE_UNCOLORED;

    @SubscribeEvent
    public static void renderBlight(RenderLivingEvent.Pre<Mob, ? extends EntityModel<? extends Mob>> event) {
        if (RENDER_TYPE_NORMAL == null) {
            RENDER_TYPE_NORMAL = RenderType.entityCutout(TEXTURE_NORMAL);
        }
        var renderType = RENDER_TYPE_NORMAL;

        LivingEntity entity = event.getEntity();
        var blightTier = entity.getData(PsAttachmentTypes.BLIGHT_TIER);
        if (blightTier > 0 && entity instanceof Mob mob) {
            renderBlightFire(renderType, mob, event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
        }
    }

    private static void renderBlightFire(RenderType renderType, Mob mob, PoseStack poseStack, MultiBufferSource pBuffer, int pPackedLight) {
        poseStack.pushPose();

        float fireScale = mob.getBbWidth() * FIRE_SCALE;
        var tempYScale = fireScale * mob.getBbHeight() * 1.2f;
        poseStack.scale(fireScale, tempYScale, fireScale);

        float hwRatio = mob.getBbHeight() / fireScale;
        float xOffset = 0.0F;
        float yOffset = (float) (mob.getY() - mob.getBoundingBox().minY);
        float zOffset = 0.0F;

        Quaternionf cam = Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation();
        poseStack.mulPose(Axis.YP.rotationDegrees(cam.y / 4f));

        poseStack.translate(0f, 0f, hwRatio * 0.02f);
        int i = 0;

        PoseStack.Pose posestack$pose = poseStack.last();
        VertexConsumer vertexconsumer = pBuffer.getBuffer(renderType);

        while (hwRatio > 0f) {
            boolean swapU = i % 2 == 0;
            int frame = ClientTicks.ticksInGame() % 32;
            float minU = swapU ? 0.5f : 0.0f;
            float minV = frame / 32f;
            float maxU = swapU ? 1.0f : 0.5f;
            float maxV = (frame + 1) / 32f;

            if (swapU) {
                var swap = maxU;
                maxU = minU;
                minU = swap;
            }

            // FIXME: layer the effect like vanilla fire
            vertex(vertexconsumer, posestack$pose, pPackedLight, 0.0F, 0, minU, maxV);
            vertex(vertexconsumer, posestack$pose, pPackedLight, 1.0F, 0, maxU, maxV);
            vertex(vertexconsumer, posestack$pose, pPackedLight, 1.0F, 1, maxU, minV);
            vertex(vertexconsumer, posestack$pose, pPackedLight, 0.0F, 1, minU, minV);

            hwRatio -= 0.45F;
            yOffset -= 0.45F;
            xOffset *= 0.9F;
            zOffset += 0.03F;
            ++i;
        }

        poseStack.popPose();
    }

    private static void vertex(VertexConsumer pConsumer, PoseStack.Pose pPose, int pPackedLight, float pX, float pY, float pU, float pV) {
        pConsumer.addVertex(pPose, pX - 0.5F, pY, 0.0F)
                .setColor(-1)
                .setUv(pU, pV)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(pPackedLight)
                .setNormal(pPose, 0.0F, 1.0F, 0.0F);
    }
}
