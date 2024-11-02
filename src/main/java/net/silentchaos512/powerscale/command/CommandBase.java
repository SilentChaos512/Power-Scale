package net.silentchaos512.powerscale.command;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class CommandBase {
    static void sendHeaderLine(CommandSourceStack source, String text) {
        source.sendSuccess(
                () -> Component.literal(text)
                        .withStyle(ChatFormatting.BOLD),
                true
        );
    }

    static void sendInfoLine(CommandSourceStack source, String prefix, String format, Object value) {
        source.sendSuccess(
                () -> {
                    var formattedSuffix = String.format(format, value);
                    var valueText = Component.literal(formattedSuffix)
                            .withStyle(ChatFormatting.GREEN);
                    return Component.literal(prefix)
                            .append(": ")
                            .append(valueText);
                },
                true
        );
    }
}
