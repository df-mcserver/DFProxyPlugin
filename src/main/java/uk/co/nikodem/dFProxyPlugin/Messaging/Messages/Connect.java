package uk.co.nikodem.dFProxyPlugin.Messaging.Messages;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ConnectionRequestBuilder;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;
import uk.co.nikodem.dFProxyPlugin.Messaging.DFPluginMessageHandler;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static uk.co.nikodem.dFProxyPlugin.Messaging.PluginMessageListener.IDENTIFIER;

public class Connect implements DFPluginMessageHandler {
    @Override
    public void run(PluginMessageEvent event, ByteArrayDataInput in) {
        if (event.getSource() instanceof ServerConnection serverConnection) {
            Player plr = serverConnection.getPlayer();
            String serverName = in.readUTF();
            Optional<RegisteredServer> serverInstance = DFProxyPlugin.server.getServer(serverName);
            if (serverInstance.isPresent()) {
                serverInstance.ifPresent(target -> {
                    ConnectionRequestBuilder request = plr.createConnectionRequest(target);
                    CompletableFuture<ConnectionRequestBuilder.Result> connection = request.connect();

                    connection.whenCompleteAsync(((result, throwable) -> {
                        if (result == null) {
                            byte[] msg = createMessage("ConnectStatus", convertBoolToString(false));
                            serverConnection.getServer().sendPluginMessage(IDENTIFIER, msg);
                            return;
                        }

                        byte[] msg = createMessage("ConnectStatus", convertBoolToString(result.isSuccessful()));
                        serverConnection.getServer().sendPluginMessage(IDENTIFIER, msg);
                    }));

                    request.fireAndForget();
                });
            } else {
                byte[] msg = createMessage("ConnectStatus", convertBoolToString(false));
                serverConnection.getServer().sendPluginMessage(IDENTIFIER, msg);
            }
        }
    }
}
