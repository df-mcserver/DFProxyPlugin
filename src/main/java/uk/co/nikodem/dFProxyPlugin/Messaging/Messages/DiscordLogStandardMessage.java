package uk.co.nikodem.dFProxyPlugin.Messaging.Messages;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;
import uk.co.nikodem.dFProxyPlugin.Discord.Utils.PluginConnectedServer;
import uk.co.nikodem.dFProxyPlugin.Messaging.DFPluginMessageHandler;

import static uk.co.nikodem.dFProxyPlugin.Messaging.PluginMessageListener.IDENTIFIER;

public class DiscordLogStandardMessage implements DFPluginMessageHandler {
    @Override
    public void run(PluginMessageEvent event, ByteArrayDataInput in) {
        if (event.getSource() instanceof ServerConnection serverConnection) {
            Player plr = serverConnection.getPlayer();
            RegisteredServer server = serverConnection.getServer();

            if (!DFProxyPlugin.config.discord_bot.isEnabled()) return;

            if (!PluginConnectedServer.isServerRegistered(server)) {
                sendPluginMessageToBackendUsingPlayer(plr, IDENTIFIER, event.getData());
                return;
            }

            String fullString = in.readUTF();

            DFProxyPlugin.discord.thread.onPluginDiscordStandardMessage(plr, server, fullString);
        }
    }
}
