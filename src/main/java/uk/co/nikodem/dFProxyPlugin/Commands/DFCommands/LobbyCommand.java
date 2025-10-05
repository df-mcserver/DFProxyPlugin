package uk.co.nikodem.dFProxyPlugin.Commands.DFCommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.*;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import uk.co.nikodem.dFProxyPlugin.Commands.DFCommand;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;
import uk.co.nikodem.dFProxyPlugin.Player.Actions.SendToServer;

public class LobbyCommand implements DFCommand {
    @Override
    public BrigadierCommand createBrigadierCommand(ProxyServer server) {
        LiteralCommandNode<CommandSource> helloNode = BrigadierCommand.literalArgumentBuilder("lobby")
                .executes(context -> {
                    if (context.getSource() instanceof Player plr) {
                        if (SendToServer.sendPlayerToServer(plr, "lobby")) plr.sendMessage(Component.text("Connecting to Lobby server..", NamedTextColor.LIGHT_PURPLE));
                        else plr.sendMessage(Component.text("Lobby server does not exist!", NamedTextColor.RED));

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
