package uk.co.nikodem.dFProxyPlugin.Messaging;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.Optional;

public interface DFPluginMessageHandler {
    void run(PluginMessageEvent event, ByteArrayDataInput in);

    default byte[] createMessage(String... messages) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        for (String msg : messages) {
            out.writeUTF(msg+" ");
        }

        return out.toByteArray();
    }

    default String convertBoolToString(boolean bool) {
        return bool ? "true" : "false";
    }

    default boolean sendPluginMessageToBackend(RegisteredServer server, ChannelIdentifier identifier, byte[] data) {
        // On success, returns true
        return server.sendPluginMessage(identifier, data);
    }

    default boolean sendPluginMessageToBackendUsingPlayer(Player player, ChannelIdentifier identifier, byte[] data) {
        Optional<ServerConnection> connection = player.getCurrentServer();
        // On success, returns true
        return connection.map(serverConnection -> serverConnection.sendPluginMessage(identifier, data)).orElse(false);
    }
}
