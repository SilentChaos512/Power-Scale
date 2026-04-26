package net.silentchaos512.powerscale.client.blightfire;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.joml.Quaternionf;

public record BlightFireSubmit(
        PoseStack.Pose pose,
        EntityRenderState entityRenderState,
        Quaternionf rotation
) {
}
