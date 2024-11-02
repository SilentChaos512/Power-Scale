package net.silentchaos512.powerscale.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.silentchaos512.powerscale.core.DifficultyUtil;
import net.silentchaos512.powerscale.setup.PsAttachmentTypes;

import java.util.Collection;

public class DifficultyCommand extends CommandBase {
    private DifficultyCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("ps_difficulty")
                        .then(
                                Commands.literal("get")
                                        .then(
                                                Commands.argument("entity", EntityArgument.entity())
                                                        .executes(
                                                                ctx -> {
                                                                    var entity = EntityArgument.getEntity(ctx, "entity");
                                                                    return printEntityDifficulty(ctx, entity);
                                                                }
                                                        )
                                        )
                                        .then(
                                                Commands.literal("local")
                                                        .executes(
                                                                DifficultyCommand::printLocalDifficulty
                                                        )
                                        )
                        )
                        .then(
                                Commands.literal("set")
                                        .then(
                                                Commands.argument("entities", EntityArgument.entities())
                                                        .then(
                                                                Commands.argument("value", DoubleArgumentType.doubleArg())
                                                                        .executes(
                                                                                ctx -> {
                                                                                    Collection<? extends Entity> entities = EntityArgument.getEntities(ctx, "entities");
                                                                                    var value = DoubleArgumentType.getDouble(ctx, "value");
                                                                                    return setDifficulty(ctx, entities, value);
                                                                                }
                                                                        )
                                                        )
                                        )
                        )
                        .then(
                                Commands.literal("add")
                                        .then(
                                                Commands.argument("entities", EntityArgument.entities())
                                                        .then(
                                                                Commands.argument("value", DoubleArgumentType.doubleArg())
                                                                        .executes(
                                                                                ctx -> {
                                                                                    Collection<? extends Entity> entities = EntityArgument.getEntities(ctx, "entities");
                                                                                    var value = DoubleArgumentType.getDouble(ctx, "value");
                                                                                    return addDifficulty(ctx, entities, value);
                                                                                }
                                                                        )
                                                        )
                                        )
                        )
                        .then(
                                Commands.literal("reset")
                                        .then(
                                                Commands.argument("entities", EntityArgument.entities())
                                                        .executes(
                                                                ctx -> {
                                                                    Collection<? extends Entity> entities = EntityArgument.getEntities(ctx, "entities");
                                                                    return resetDifficulty(ctx, entities);
                                                                }
                                                        )
                                        )
                        )
        );
    }

    private static int printEntityDifficulty(CommandContext<CommandSourceStack> context, Entity entity) {
        var difficulty = entity.getData(PsAttachmentTypes.DIFFICULTY);
        sendInfoLine(context.getSource(), entity.getName().getString() + "'s difficulty", "%.3f", difficulty);
        return 1;
    }

    private static int printLocalDifficulty(CommandContext<CommandSourceStack> context) {
        var source = context.getSource();
        var localDifficulty = DifficultyUtil.getLocalDifficulty(source.getLevel(), source.getPosition());
        sendInfoLine(source, "Local Difficulty", "%.3f", localDifficulty);
        return 1;
    }

    private static int setDifficulty(CommandContext<CommandSourceStack> context, Collection<? extends Entity> entities, double value) {
        for (var entity : entities) {
            var clampedResult = DifficultyUtil.setDifficultyClamped(entity, value);
            sendInfoLine(context.getSource(), "Set " + entity.getName().getString() + "'s difficulty to", "%.3f", clampedResult);
        }
        return 1;
    }

    private static int addDifficulty(CommandContext<CommandSourceStack> context, Collection<? extends Entity> entities, double value) {
        for (var entity : entities) {
            var newValue = DifficultyUtil.getDifficulty(entity) + value;
            var clampedResult = DifficultyUtil.setDifficultyClamped(entity, newValue);
            sendInfoLine(context.getSource(), "Set " + entity.getName().getString() + "'s difficulty to", "%.3f", clampedResult);
        }
        return 1;
    }

    private static int resetDifficulty(CommandContext<CommandSourceStack> context, Collection<? extends Entity> entities) {
        for (var entity : entities) {
            DifficultyUtil.resetDifficulty(entity);
            var newValue = DifficultyUtil.getDifficulty(entity);
            sendInfoLine(context.getSource(), "Reset " + entity.getScoreboardName() + "'s difficulty to", "%.3f", newValue);
        }
        return 1;
    }
}
