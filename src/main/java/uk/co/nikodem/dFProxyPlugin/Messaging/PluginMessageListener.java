package uk.co.nikodem.dFProxyPlugin.Messaging;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ConnectionRequestBuilder;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class PluginMessageListener {
    public static final MinecraftChannelIdentifier IDENTIFIER = MinecraftChannelIdentifier.from("df:proxy");

    public static void onPluginMessage(PluginMessageEvent event) {
        if (!IDENTIFIER.equals(event.getIdentifier())) {
            return;
        }
        event.setResult(PluginMessageEvent.ForwardResult.handled());

        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        String command = in.readUTF();

        switch (command) {
            case "IsGeyser":
                if (event.getSource() instanceof Player plr) {
                    boolean isUnderGeyser = DFProxyPlugin.geyser.isBedrockPlayer(plr.getUniqueId());
                    byte[] msg = createMessage("IsGeyser", convertBoolToString(isUnderGeyser));
                    plr.getCurrentServer().ifPresent(serverConnection -> serverConnection.getServer().sendPluginMessage(IDENTIFIER, msg));
                }
            case "Connect":
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

            default:
                return;
        }
    }

    public static byte[] createMessage(String... messages) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        for (String msg : messages) {
            out.writeUTF(msg+" ");
        }

        return out.toByteArray();
    }

    public static String convertBoolToString(boolean bool) {
        return bool ? "true" : "false";
    }
}
