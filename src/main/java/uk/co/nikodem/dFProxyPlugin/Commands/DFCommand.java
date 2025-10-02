package uk.co.nikodem.dFProxyPlugin.Commands;

import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.ProxyServer;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;

public interface DFCommand {
    BrigadierCommand createBrigadierCommand(final ProxyServer server);
    CommandMeta createCommandMeta(final DFProxyPlugin plugin, final CommandManager commandManager);

    public static void registerDFCommand(final DFProxyPlugin plugin, final DFCommand command) {
        ProxyServer server = DFProxyPlugin.server;
        CommandManager commandManager = DFProxyPlugin.commandManager;
        BrigadierCommand brigadierCommand = command.createBrigadierCommand(server);
        CommandMeta meta = command.createCommandMeta(plugin, commandManager);

        commandManager.register(meta, brigadierCommand);
    }
}
