package net.silentchaos512.powerscale.client.blightfire;

import com.google.common.reflect.TypeToken;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.AtlasManager;
import net.minecraft.client.resources.model.Material;
import net.minecraft.util.Mth;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import net.neoforged.neoforge.common.util.Lazy;
import net.silentchaos512.powerscale.PowerScale;
import org.joml.Quaternionf;

@EventBusSubscriber(value = Dist.CLIENT)
public class BlightFireRenderer {
    public static final ContextKey<Boolean> FIRE_CONTEXT_KEY = new ContextKey<>(PowerScale.getId("blight_fire"));

    private static final Lazy<Material> FIRE_0 = Lazy.of(() -> new Material(TextureAtlas.LOCATION_BLOCKS, PowerScale.getId("entity/blight_fire_0")));
    private static final Lazy<Material> FIRE_1 = Lazy.of(() -> new Material(TextureAtlas.LOCATION_BLOCKS, PowerScale.getId("entity/blight_fire_1")));

    @SubscribeEvent
    public static void onRegisterRenderStateModifiers(RegisterRenderStateModifiersEvent event) {
        event.registerEntityModifier(
                new TypeToken<EntityRenderer<? extends Entity, ? extends EntityRenderState>>() {},
                (mob, renderState) -> {
//                    if (mob.hasData(PsAttachmentTypes.IS_BLIGHT) && mob.getData(PsAttachmentTypes.IS_BLIGHT)) {
                        renderState.setRenderData(FIRE_CONTEXT_KEY, true);
//                    }
                }
        );
    }

    @SubscribeEvent
    public static void onRenderMob(RenderLivingEvent.Pre<Mob, ? extends LivingEntityRenderState, ?> event) {
        if (isBlight(event.getRenderState())) {
            var mc = Minecraft.getInstance();
            var cameraRenderState = mc.gameRenderer.getLevelRenderState().cameraRenderState;
            var rotation = Mth.rotationAroundAxis(Mth.Y_AXIS, cameraRenderState.orientation, new Quaternionf());
            renderFlame(
                    event.getPoseStack().last().copy(),
                    mc.renderBuffers().bufferSource(),
                    event.getRenderState(),
                    rotation,
                    mc.getAtlasManager()
            );
        }
    }

    private static boolean isBlight(LivingEntityRenderState renderState) {
        return renderState.getRenderDataOrDefault(FIRE_CONTEXT_KEY, false);
    }

    // Largely copied from net.minecraft.client.renderer.feature.FlameFeatureRenderer
    private static void renderFlame(PoseStack.Pose pose, MultiBufferSource bufferSource, EntityRenderState renderState, Quaternionf rotation, AtlasManager atlasManager) {
        float f = renderState.boundingBoxWidth * 1.4F;
        pose.scale(f, f, f);
        float f1 = 0.5F;
        float f2 = 0.0F;
        float f3 = renderState.boundingBoxHeight / f;
        float f4 = 0.0F;
        pose.rotate(rotation);
        pose.translate(0.0F, 0.0F, 0.3F - (int) f3 * 0.02F);
        float f5 = 0.0F;
        int i = 0;

        for (VertexConsumer vertexconsumer = bufferSource.getBuffer(Sheets.cutoutBlockSheet()); f3 > 0.0F; i++) {
            TextureAtlasSprite fireSprite = i % 2 == 0 ? atlasManager.get(FIRE_0.get()) : atlasManager.get(FIRE_1.get());
            float f6 = fireSprite.getU0();
            float f7 = fireSprite.getV0();
            float f8 = fireSprite.getU1();
            float f9 = fireSprite.getV1();
            if (i / 2 % 2 == 0) {
                float f10 = f8;
                f8 = f6;
                f6 = f10;
            }

            fireVertex(pose, vertexconsumer, -f1 - 0.0F, 0.0F - f4, f5, f8, f9);
            fireVertex(pose, vertexconsumer, f1 - 0.0F, 0.0F - f4, f5, f6, f9);
            fireVertex(pose, vertexconsumer, f1 - 0.0F, 1.4F - f4, f5, f6, f7);
            fireVertex(pose, vertexconsumer, -f1 - 0.0F, 1.4F - f4, f5, f8, f7);
            f3 -= 0.45F;
            f4 -= 0.45F;
            f1 *= 0.9F;
            f5 -= 0.03F;
        }
    }

    // Copied from FlameFeatureRenderer
    private static void fireVertex(
            PoseStack.Pose pose, VertexConsumer consumer, float x, float y, float z, float u, float v
    ) {
        consumer.addVertex(pose, x, y, z)
                .setColor(-1)
                .setUv(u, v)
                .setUv1(0, 10)
                .setLight(240)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }
}
