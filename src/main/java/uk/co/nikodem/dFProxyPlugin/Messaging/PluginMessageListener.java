package uk.co.nikodem.dFProxyPlugin.Messaging;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import uk.co.nikodem.dFProxyPlugin.Messaging.Messages.Connect;
import uk.co.nikodem.dFProxyPlugin.Messaging.Messages.IncompatibleClient;
import uk.co.nikodem.dFProxyPlugin.Messaging.Messages.IsGeyser;
import uk.co.nikodem.dFProxyPlugin.Messaging.Messages.RealProtocolVersion;

import java.util.HashMap;

public class PluginMessageListener {
    public static final MinecraftChannelIdentifier IDENTIFIER = MinecraftChannelIdentifier.from("df:proxy");
    public static final HashMap<String, DFPluginMessageHandler> messageHandlers = new HashMap<>();

    public static void initialiseMessageHandlers() {
        messageHandlers.put("RealProtocolVersion", new RealProtocolVersion());
        messageHandlers.put("IncompatibleClient", new IncompatibleClient());
        messageHandlers.put("IsGeyser", new IsGeyser());
        messageHandlers.put("Connect", new Connect());
    }

    public static void onPluginMessage(PluginMessageEvent event) {
        if (!IDENTIFIER.equals(event.getIdentifier())) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        String command = in.readUTF();

        DFPluginMessageHandler handler = messageHandlers.get(command);

        if (handler != null) {
            handler.run(event, in);
        }

        event.setResult(PluginMessageEvent.ForwardResult.handled());
    }
}
