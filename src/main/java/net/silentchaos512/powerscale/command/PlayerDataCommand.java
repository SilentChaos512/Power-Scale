package net.silentchaos512.powerscale.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.silentchaos512.powerscale.core.DifficultyUtil;
import net.silentchaos512.powerscale.setup.PsAttachmentTypes;

import java.util.Collection;

public final class PlayerDataCommand extends CommandBase {
    private PlayerDataCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("ps_player")
                        .then(
                                Commands.literal("info")
                                        .then(
                                                Commands.argument("players", EntityArgument.players())
                                                        .executes(
                                                                ctx -> runInfo(ctx, EntityArgument.getPlayers(ctx, "players"))
                                                        )
                                        )
                                        .executes(
                                                ctx -> runInfo(ctx, ctx.getSource().getPlayer())
                                        )
                        )
        );
    }

    private static int runInfo(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> players) {
        for (var player : players) {
            sendPlayerInfo(context.getSource(), player);
        }
        return 1;
    }

    private static int runInfo(CommandContext<CommandSourceStack> context, ServerPlayer player) {
        sendPlayerInfo(context.getSource(), player);
        return 1;
    }

    private static void sendPlayerInfo(CommandSourceStack source, ServerPlayer player) {
        sendHeaderLine(source, player.getName().getString() + "'s SAD Data");
        sendInfoLine(source, "Difficulty", "%.3f", player.getData(PsAttachmentTypes.DIFFICULTY));
        sendInfoLine(source, "Local Difficulty", "%.3f", DifficultyUtil.getLocalDifficulty(player.level(), player.getOnPos()));
        sendInfoLine(source, "Level", "%d", player.getData(PsAttachmentTypes.LEVEL));
        sendInfoLine(source, "SP", "%d", player.getData(PsAttachmentTypes.SP));
    }
}
