package uk.co.nikodem.dFProxyPlugin.Commands.DFCommands.Banning;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.*;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import uk.co.nikodem.dFProxyPlugin.Bans.BanInformation;
import uk.co.nikodem.dFProxyPlugin.Bans.TimeManager;
import uk.co.nikodem.dFProxyPlugin.Commands.DFCommand;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;
import uk.co.nikodem.dFProxyPlugin.Player.Data.UUIDConversionHandler;

import java.util.Date;
import java.util.UUID;

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
                            String playerArgument = context.getArgument("player", String.class);
                            UUID uuidToBan = getUUID(server, playerArgument);

                            doBan(server, context.getSource(), uuidToBan, BanInformation.createInformation(uuidToBan));

                            return Command.SINGLE_SUCCESS;
                        })
                        .then(BrigadierCommand.requiredArgumentBuilder("time", StringArgumentType.word())
                                .executes(context -> {
                                    String playerArgument = context.getArgument("player", String.class);
                                    String timeArgument = context.getArgument("time", String.class);
                                    UUID uuidToBan = getUUID(server, playerArgument);
                                    long end = new Date().getTime() + TimeManager.formatInputIntoDuration(timeArgument);

                                    doBan(server, context.getSource(), uuidToBan, BanInformation.createInformation(uuidToBan, end));

                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(BrigadierCommand.requiredArgumentBuilder("reason", StringArgumentType.greedyString())
                                        .executes(context -> {
                                            String playerArgument = context.getArgument("player", String.class);
                                            String timeArgument = context.getArgument("time", String.class);
                                            String reasonArgument = context.getArgument("reason", String.class);
                                            UUID uuidToBan = getUUID(server, playerArgument);
                                            long end = new Date().getTime() + TimeManager.formatInputIntoDuration(timeArgument);

                                            doBan(server, context.getSource(), uuidToBan, BanInformation.createInformation(uuidToBan, end, reasonArgument));

                                            return Command.SINGLE_SUCCESS;
                                        })
                                ))
                )
                .build();

        return new BrigadierCommand(helloNode);
    }

    public UUID getUUID(final ProxyServer server, String playerArgument) {
        return server.getPlayer(playerArgument).isPresent()
                ? server.getPlayer(playerArgument).get().getUniqueId()
                : UUIDConversionHandler.convertUsernameOrStringIntoUUID(playerArgument);
    }

    public void doBan(final ProxyServer server, CommandSource source, UUID uuid, BanInformation info) {
        if (uuid != null) {
            DFProxyPlugin.banManager.setBanInformation(uuid, info);

            server.getPlayer(uuid).ifPresent((plr) -> plr.disconnect(info.getBanMessage()));

            Component message = Component.text("Successfully banned player!", NamedTextColor.GREEN);
            source.sendMessage(message);
        } else {
            Component message = Component.text("Invalid player!", NamedTextColor.RED);
            source.sendMessage(message);
        }
    }

    @Override
    public CommandMeta createCommandMeta(final DFProxyPlugin plugin, final CommandManager commandManager) {
        return commandManager.metaBuilder("ban")
                .plugin(plugin)
                .build();
    }
}
