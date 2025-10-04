package uk.co.nikodem.dFProxyPlugin.Commands.DFCommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.*;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import uk.co.nikodem.dFProxyPlugin.Commands.DFCommand;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;
import uk.co.nikodem.dFProxyPlugin.Player.Data.PlayerDataHandler;

import java.util.Objects;

public class RequestDataCommand implements DFCommand {
    @Override
    public BrigadierCommand createBrigadierCommand(final ProxyServer server) {
        LiteralCommandNode<CommandSource> helloNode = BrigadierCommand.literalArgumentBuilder("request_data")
                .executes(context -> {
                    CommandSource source = context.getSource();

                    if (source instanceof Player target) {
                        target.sendMessage(
                                Component.text(Objects.requireNonNull(PlayerDataHandler.readPlayerFileAsString(target)))
                        );
                    }

                    return Command.SINGLE_SUCCESS;
                })
                .build();

        return new BrigadierCommand(helloNode);
    }

    @Override
    public CommandMeta createCommandMeta(DFProxyPlugin plugin, CommandManager commandManager) {
        return commandManager.metaBuilder("request_data")
                .plugin(plugin)
                .build();
    }
}
