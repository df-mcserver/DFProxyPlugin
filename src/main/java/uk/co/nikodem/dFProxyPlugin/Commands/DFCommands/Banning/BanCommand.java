package uk.co.nikodem.dFProxyPlugin.Commands.DFCommands.Banning;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.*;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import uk.co.nikodem.dFProxyPlugin.Bans.BannedPlayer;
import uk.co.nikodem.dFProxyPlugin.Commands.DFCommand;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;
import uk.co.nikodem.dFProxyPlugin.Player.Data.UUIDConversionHandler;

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

                            if (uuidToBan != null) {
                                BannedPlayer info = BannedPlayer.createInformation(uuidToBan);
                                DFProxyPlugin.banManager.setBanInformation(uuidToBan, info);

                                kickPlayerIfPossible(server, uuidToBan, info);

                                Component message = Component.text("Successfully banned player!", NamedTextColor.GREEN);
                                context.getSource().sendMessage(message);
                            } else {
                                Component message = Component.text("Invalid player!", NamedTextColor.RED);
                                context.getSource().sendMessage(message);
                            }

                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();

        return new BrigadierCommand(helloNode);
    }

    public UUID getUUID(final ProxyServer server, String playerArgument) {
        return server.getPlayer(playerArgument).isPresent()
                ? server.getPlayer(playerArgument).get().getUniqueId()
                : UUIDConversionHandler.convertUsernameOrStringIntoUUID(playerArgument);
    }

    public void kickPlayerIfPossible(final ProxyServer server, UUID uuid, BannedPlayer info) {
        server.getPlayer(uuid).ifPresent((plr) -> plr.disconnect(info.getBanMessage()));
    }

    @Override
    public CommandMeta createCommandMeta(final DFProxyPlugin plugin, final CommandManager commandManager) {
        return commandManager.metaBuilder("ban")
                .plugin(plugin)
                .build();
    }
}
