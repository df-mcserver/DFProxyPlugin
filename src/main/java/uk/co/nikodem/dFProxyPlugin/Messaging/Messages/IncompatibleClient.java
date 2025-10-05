package uk.co.nikodem.dFProxyPlugin.Messaging.Messages;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import uk.co.nikodem.dFProxyPlugin.Messaging.DFPluginMessageHandler;
import uk.co.nikodem.dFProxyPlugin.Player.Platform.ParsedPlatformInformation;

import static uk.co.nikodem.dFProxyPlugin.Messaging.PluginMessageListener.IDENTIFIER;

public class IncompatibleClient implements DFPluginMessageHandler {
    @Override
    public void run(PluginMessageEvent event, ByteArrayDataInput in) {
        if (event.getSource() instanceof ServerConnection serverConnection) {
            Player plr = serverConnection.getPlayer();
            boolean isIncompatible = ParsedPlatformInformation.fromPlayer(plr).isIncompatible();

            byte[] msg = createMessage("IncompatibleClient", convertBoolToString(isIncompatible));

            sendPluginMessageToBackendUsingPlayer(plr, IDENTIFIER, msg);
        }
    }
}
