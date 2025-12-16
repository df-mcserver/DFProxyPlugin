package uk.co.nikodem.dFProxyPlugin.Commands.DFCommands.Multiplayer;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import uk.co.nikodem.dFProxyPlugin.Commands.DFCommand;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;

public class PlayerlistCommand implements DFCommand {
    @Override
    public BrigadierCommand createBrigadierCommand(final ProxyServer server) {
        LiteralCommandNode<CommandSource> helloNode = BrigadierCommand.literalArgumentBuilder("playerlist")
                .executes(context -> {
                    CommandSource source = context.getSource();

                    source.sendMessage(
                            MiniMessage.miniMessage().deserialize(
                                    createResultString(server)
                            )
                    );

                    return Command.SINGLE_SUCCESS;
                })
                .build();

        return new BrigadierCommand(helloNode);
    }

    private static @NotNull String createResultString(ProxyServer server) {
        int totalPlayerCount = server.getAllPlayers().size();
        StringBuilder finalString = new StringBuilder("<aqua>Online players ("+totalPlayerCount+"):</aqua>");

        for (RegisteredServer regServer : server.getAllServers()) {
            int playerCount = regServer.getPlayersConnected().size();
            if (playerCount < 1) continue;
            String name = regServer.getServerInfo().getName();
            StringBuilder serverString = new StringBuilder("<br><u>"+name+" ("+playerCount+"/"+totalPlayerCount+")</u>");

            for (Player plr : regServer.getPlayersConnected()) {
                serverString.append("<br>-  ").append(plr.getUsername());
            }

            finalString.append(serverString);
        }

        return finalString.toString();
    }

    @Override
    public CommandMeta createCommandMeta(DFProxyPlugin plugin, CommandManager commandManager) {
        return commandManager.metaBuilder("playerlist")
                .aliases("players", "online_players", "online")
                .plugin(plugin)
                .build();
    }
}
