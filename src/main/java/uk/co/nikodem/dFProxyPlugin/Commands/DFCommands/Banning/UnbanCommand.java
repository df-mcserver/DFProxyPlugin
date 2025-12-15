package uk.co.nikodem.dFProxyPlugin.Commands.DFCommands.Banning;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import uk.co.nikodem.dFProxyPlugin.Commands.DFCommand;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;

import java.util.UUID;

public class UnbanCommand implements DFCommand {
    @Override
    public BrigadierCommand createBrigadierCommand(final ProxyServer server) {
        LiteralCommandNode<CommandSource> helloNode = BrigadierCommand.literalArgumentBuilder("unban")
                .requires(source -> source.hasPermission("dfproxy.ban"))
                .executes(context -> {
                    CommandSource source = context.getSource();

                    Component message = Component.text("No player provided!", NamedTextColor.RED);
                    source.sendMessage(message);

                    return Command.SINGLE_SUCCESS;
                })
                .then(BrigadierCommand.requiredArgumentBuilder("player", StringArgumentType.word())
                        .executes(context -> {
                            String playerArgument = context.getArgument("player", String.class);
                            UUID uuidToUnban = getUUID(playerArgument);

                            if (uuidToUnban != null) {
                                DFProxyPlugin.banManager.removeBanInformation(uuidToUnban);

                                Component message = Component.text("Successfully unbanned player!", NamedTextColor.GREEN);
                                context.getSource().sendMessage(message);
                            } else {
                                Component message = Component.text("Invalid player!", NamedTextColor.RED);
                                context.getSource().sendMessage(message);
                            }

                            return Command.SINGLE_SUCCESS;
                        }))
                .build();

        return new BrigadierCommand(helloNode);
    }

    public UUID getUUID(String playerArgument) {
        return DFProxyPlugin.uuidConversionHandler.convertUsernameOrStringIntoUUID(playerArgument);
    }

    @Override
    public CommandMeta createCommandMeta(final DFProxyPlugin plugin, final CommandManager commandManager) {
        return commandManager.metaBuilder("unban")
                .plugin(plugin)
                .build();
    }
}
