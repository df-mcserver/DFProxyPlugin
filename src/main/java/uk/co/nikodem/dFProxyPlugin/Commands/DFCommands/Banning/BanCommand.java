package uk.co.nikodem.dFProxyPlugin.Commands.DFCommands.Banning;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.*;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
//import uk.co.nikodem.dFProxyPlugin.BanlistManager;
import uk.co.nikodem.dFProxyPlugin.Commands.DFCommand;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;

public class BanCommand implements DFCommand {
    @Override
    public BrigadierCommand createBrigadierCommand(final ProxyServer server) {
        LiteralCommandNode<CommandSource> helloNode = BrigadierCommand.literalArgumentBuilder("ban")
                .requires(source -> source.hasPermission("dfproxy.ban"))
                .executes(context -> {
                    CommandSource source = context.getSource();

                    Component message = Component.text("No player provided!", NamedTextColor.RED);
                    source.sendMessage(message);

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
                            server.getPlayer(argumentProvided).ifPresent((plr) -> {
//                                        BanlistManager.banPlayer(plr);
//                                        plr.disconnect(BanlistManager.createBanMessage(plr));
                                    }
                            );

                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();

        return new BrigadierCommand(helloNode);
    }

    @Override
    public CommandMeta createCommandMeta(final DFProxyPlugin plugin, final CommandManager commandManager) {
        return commandManager.metaBuilder("ban")
                .plugin(plugin)
                .build();
    }
}
