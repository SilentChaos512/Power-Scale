package net.silentchaos512.powerscale.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.silentchaos512.powerscale.core.resources.DataHolder;
import net.silentchaos512.powerscale.core.resources.ScalingAttributeManager;
import net.silentchaos512.powerscale.core.scalingattribute.ScalingAttribute;
import net.silentchaos512.powerscale.core.scalingattribute.ScalingAttributeHelper;
import net.silentchaos512.powerscale.setup.PsAttachmentTypes;
import net.silentchaos512.powerscale.setup.PsRegistries;

import java.util.HashMap;
import java.util.Map;

public class ScalingAttributeCommand extends CommandBase {
    private ScalingAttributeCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(
                Commands.literal("ps_attributes")
                        .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                        .then(
                                Commands.argument("target", EntityArgument.entity())
                                        .then(
                                                Commands.argument("attribute", ResourceLocationArgument.id())
                                                        .suggests(
                                                                (ctx, builder) ->
                                                                        SharedSuggestionProvider.suggestResource(PsRegistries.SCALING_ATTRIBUTE.keySet(), builder)
                                                        )
                                                        .then(
                                                                Commands.literal("get")
                                                                        .executes(
                                                                                ctx -> getAttributeBoost(
                                                                                        ctx.getSource(),
                                                                                        EntityArgument.getEntity(ctx, "target"),
                                                                                        ResourceLocationArgument.getId(ctx, "attribute")
                                                                                )
                                                                        )
                                                        )
                                                        .then(
                                                                Commands.literal("set")
                                                                        .then(
                                                                                Commands.argument("amount", DoubleArgumentType.doubleArg())
                                                                                        .executes(
                                                                                                ctx -> setAttributeBoost(
                                                                                                        ctx.getSource(),
                                                                                                        EntityArgument.getEntity(ctx, "target"),
                                                                                                        ResourceLocationArgument.getId(ctx, "attribute"),
                                                                                                        DoubleArgumentType.getDouble(ctx, "amount")
                                                                                                )
                                                                                        )
                                                                        )
                                                        )
                                        )
                        )
        );
    }

    private static int getAttributeBoost(CommandSourceStack source, Entity target, ResourceLocation attributeId) {
        DataHolder<ScalingAttribute> scalingAttribute = ScalingAttributeManager.getHolder(attributeId);
        Map<DataHolder<ScalingAttribute>, Double> boostedAttributes = target.getExistingData(PsAttachmentTypes.BOOSTED_ATTRIBUTES).orElse(null);

        if (!scalingAttribute.isPresent()) {
            source.sendFailure(Component.literal("Scaling Attribute does not exist for " + attributeId));
            return 0;
        }
        if (boostedAttributes == null) {
            source.sendFailure(Component.literal("Target has no boosted attributes"));
            return 0;
        }

        var amount = boostedAttributes.get(scalingAttribute);
        var attributeName = scalingAttribute.get().getName();
        var amountText = Component.literal((amount > 0 ? "+" : "") + amount);
        source.sendSuccess(() -> Component.translatable("item.powerscale.mutator.desc", attributeName, amountText), true);
        return 1;
    }

    private static int setAttributeBoost(CommandSourceStack source, Entity target, ResourceLocation attributeId, double amount) {
        if (!(target instanceof LivingEntity livingEntity)) {
            source.sendFailure(Component.literal("Target is not a living entity!"));
            return 0;
        }

        DataHolder<ScalingAttribute> scalingAttribute = ScalingAttributeManager.getHolder(attributeId);
        if (!scalingAttribute.isPresent()) {
            source.sendFailure(Component.literal("Scaling Attribute does not exist for " + attributeId));
            return 0;
        }

        Map<DataHolder<ScalingAttribute>, Double> immutableMap = target.getData(PsAttachmentTypes.BOOSTED_ATTRIBUTES);
        var boostedAttributes = new HashMap<>(immutableMap);

        boostedAttributes.put(scalingAttribute, amount);
        target.setData(PsAttachmentTypes.BOOSTED_ATTRIBUTES, boostedAttributes);
        ScalingAttributeHelper.applyBoostedAttributes(livingEntity);
        return 1;
    }
}
