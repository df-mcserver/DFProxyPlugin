package uk.co.nikodem.dFProxyPlugin.Commands.DFCommands.Data;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.*;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import uk.co.nikodem.dFProxyPlugin.Commands.DFCommand;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;
import uk.co.nikodem.dFProxyPlugin.Player.Platform.ParsedPlatformInformation;

public class PlatformCommand implements DFCommand {
    @Override
    public BrigadierCommand createBrigadierCommand(final ProxyServer server) {
        LiteralCommandNode<CommandSource> helloNode = BrigadierCommand.literalArgumentBuilder("platform")
                .executes(context -> {
                    CommandSource source = context.getSource();

                    if (source instanceof Player target) {
                        sendPlatformInformation(source, target);
                    }

                    return Command.SINGLE_SUCCESS;
                })

                .then(BrigadierCommand.requiredArgumentBuilder("player", StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            server.getAllPlayers().forEach(player -> builder.suggest(
                                    player.getUsername(),
                                    VelocityBrigadierMessage.tooltip(
                                            MiniMessage.miniMessage().deserialize(player.getUsername())
                                    )
                            ));
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            String argumentProvided = context.getArgument("player", String.class);
                            if (server.getPlayer(argumentProvided).isEmpty()) {
                                context.getSource().sendMessage(Component.text("Invalid player", NamedTextColor.RED));
                                return Command.SINGLE_SUCCESS;
                            }
                            Player target = server.getPlayer(argumentProvided).get();

                            sendPlatformInformation(context.getSource(), target);

                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();

        return new BrigadierCommand(helloNode);
    }

    @Override
    public CommandMeta createCommandMeta(DFProxyPlugin plugin, CommandManager commandManager) {
        return commandManager.metaBuilder("platform")
                .plugin(plugin)
                .build();
    }

    public void sendPlatformInformation(CommandSource source, Player plr) {
        ParsedPlatformInformation info = ParsedPlatformInformation.fromPlayer(plr);
        String msg = info.toString();

        source.sendMessage(Component.text(msg, info.isBedrock() ? NamedTextColor.DARK_PURPLE : NamedTextColor.LIGHT_PURPLE));
    }
}
