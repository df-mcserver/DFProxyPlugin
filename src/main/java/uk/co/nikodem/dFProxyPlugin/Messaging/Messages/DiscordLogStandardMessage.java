package uk.co.nikodem.dFProxyPlugin.Messaging.Messages;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;
import uk.co.nikodem.dFProxyPlugin.Messaging.DFPluginMessageHandler;

public class DiscordLogStandardMessage implements DFPluginMessageHandler {
    @Override
    public void run(PluginMessageEvent event, ByteArrayDataInput in) {
        if (event.getSource() instanceof ServerConnection serverConnection) {
            Player plr = serverConnection.getPlayer();
            RegisteredServer server = serverConnection.getServer();

            StringBuilder fullString = new StringBuilder();
            boolean endOfFile = false;
            while (!endOfFile) {
                String readLine = in.readLine();
                if (readLine == null) endOfFile = true;
                else fullString.append(readLine);
            }

            DFProxyPlugin.discord.thread.onPluginDiscordStandardMessage(plr, server, fullString.toString());
        }
    }
}
