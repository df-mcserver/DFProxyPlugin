package uk.co.nikodem.dFProxyPlugin.Messaging.Messages;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;
import uk.co.nikodem.dFProxyPlugin.Messaging.DFPluginMessageHandler;

import static uk.co.nikodem.dFProxyPlugin.Messaging.PluginMessageListener.IDENTIFIER;

public class IsGeyser implements DFPluginMessageHandler {

    @Override
    public void run(PluginMessageEvent event, ByteArrayDataInput in) {
        if (event.getSource() instanceof ServerConnection serverConnection) {
            Player plr = serverConnection.getPlayer();
            boolean isUnderGeyser = DFProxyPlugin.geyser.isBedrockPlayer(plr.getUniqueId());
            byte[] msg = createMessage("IsGeyser", convertBoolToString(isUnderGeyser));
            sendPluginMessageToBackend(serverConnection.getServer(), IDENTIFIER, msg);
        }
    }
}
