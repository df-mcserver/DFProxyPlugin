package uk.co.nikodem.dFProxyPlugin.Commands.DFCommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.*;
import com.velocitypowered.api.proxy.ConnectionRequestBuilder;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import uk.co.nikodem.dFProxyPlugin.Commands.DFCommand;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;

import java.util.Optional;

public class LobbyCommand implements DFCommand {
    @Override
    public BrigadierCommand createBrigadierCommand(ProxyServer server) {
        LiteralCommandNode<CommandSource> helloNode = BrigadierCommand.literalArgumentBuilder("lobby")
                .executes(context -> {
                    if (context.getSource() instanceof Player plr) {
                        Optional<RegisteredServer> lobbyMaybe = server.getServer("lobby");
                        if (!lobbyMaybe.isPresent()) {
                            plr.sendMessage(Component.text("Lobby server does not exist!", NamedTextColor.RED));
                            return Command.SINGLE_SUCCESS;
                        }

                        // TODO: finish lobby commmand

                        RegisteredServer lobby = lobbyMaybe.get();
                        ConnectionRequestBuilder request = plr.createConnectionRequest(lobby);

                        request.fireAndForget();
                        plr.sendMessage(Component.text("Connecting to Lobby server..", NamedTextColor.LIGHT_PURPLE));
                        return Command.SINGLE_SUCCESS;
                    }

                    return Command.SINGLE_SUCCESS;
                })
                .build();

        return new BrigadierCommand(helloNode);
    }

    @Override
    public CommandMeta createCommandMeta(DFProxyPlugin plugin, CommandManager commandManager) {
        return commandManager.metaBuilder("lobby")
                .aliases("hub", "l")
                .plugin(plugin)
                .build();
    }
}
