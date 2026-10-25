package uk.co.nikodem.dFProxyPlugin.Messaging.Messages;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import uk.co.nikodem.dFProxyPlugin.Messaging.DFPluginMessageHandler;

import static uk.co.nikodem.dFProxyPlugin.Messaging.PluginMessageListener.IDENTIFIER;

public class PlayerOpenQuickActionsDialog implements DFPluginMessageHandler {
    @Override
    public void run(PluginMessageEvent event, ByteArrayDataInput in) {

    }

    public void sendRequest(Player plr) {
        byte[] msg = createMessage("PlayerOpenQuickActionsDialog");

        this.sendPluginMessageToBackendUsingPlayer(plr, IDENTIFIER, msg);
    }
}
