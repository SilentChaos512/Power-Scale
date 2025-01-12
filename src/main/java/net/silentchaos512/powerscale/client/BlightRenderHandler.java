package net.silentchaos512.powerscale.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
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
        var isBlight = entity.getData(PsAttachmentTypes.IS_BLIGHT);
        if (isBlight && entity instanceof Mob mob) {
//            renderBlightFire(renderType, mob, event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
            var pQuaternion = Mth.rotationAroundAxis(Mth.Y_AXIS, Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation(), new Quaternionf());
            renderFlame(event.getPoseStack(), event.getMultiBufferSource(), mob, pQuaternion);
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

    public static final Material FIRE_0 = new Material(TextureAtlas.LOCATION_BLOCKS, PowerScale.getId("entity/blight_fire_0"));
    public static final Material FIRE_1 = new Material(TextureAtlas.LOCATION_BLOCKS, PowerScale.getId("entity/blight_fire_1"));

    private static void renderFlame(PoseStack pPoseStack, MultiBufferSource pBuffer, Entity pEntity, Quaternionf pQuaternion) {
        TextureAtlasSprite textureatlassprite = FIRE_0.sprite();
        TextureAtlasSprite textureatlassprite1 = FIRE_1.sprite();
        pPoseStack.pushPose();
        float f = pEntity.getBbWidth() * 1.4F;
        pPoseStack.scale(f, f, f);
        float f1 = 0.5F;
        float f2 = 0.0F;
        float f3 = pEntity.getBbHeight() / f;
        float f4 = 0.0F;
        pPoseStack.mulPose(pQuaternion);
        pPoseStack.translate(0.0F, 0.0F, 0.3F - (float)((int)f3) * 0.02F);
        float f5 = 0.0F;
        int i = 0;
        VertexConsumer vertexconsumer = pBuffer.getBuffer(Sheets.cutoutBlockSheet());

        for (PoseStack.Pose posestack$pose = pPoseStack.last(); f3 > 0.0F; i++) {
            TextureAtlasSprite textureatlassprite2 = i % 2 == 0 ? textureatlassprite : textureatlassprite1;
            float f6 = textureatlassprite2.getU0();
            float f7 = textureatlassprite2.getV0();
            float f8 = textureatlassprite2.getU1();
            float f9 = textureatlassprite2.getV1();
            if (i / 2 % 2 == 0) {
                float f10 = f8;
                f8 = f6;
                f6 = f10;
            }

            fireVertex(posestack$pose, vertexconsumer, -f1 - 0.0F, 0.0F - f4, f5, f8, f9);
            fireVertex(posestack$pose, vertexconsumer, f1 - 0.0F, 0.0F - f4, f5, f6, f9);
            fireVertex(posestack$pose, vertexconsumer, f1 - 0.0F, 1.4F - f4, f5, f6, f7);
            fireVertex(posestack$pose, vertexconsumer, -f1 - 0.0F, 1.4F - f4, f5, f8, f7);
            f3 -= 0.45F;
            f4 -= 0.45F;
            f1 *= 0.9F;
            f5 -= 0.03F;
        }

        pPoseStack.popPose();
    }

    private static void fireVertex(PoseStack.Pose pMatrixEntry, VertexConsumer pBuffer, float pX, float pY, float pZ, float pTexU, float pTexV) {
        pBuffer.addVertex(pMatrixEntry, pX, pY, pZ)
                .setColor(-1)
                .setUv(pTexU, pTexV)
                .setUv1(0, 10)
                .setLight(240)
                .setNormal(pMatrixEntry, 0.0F, 1.0F, 0.0F);
    }
}
