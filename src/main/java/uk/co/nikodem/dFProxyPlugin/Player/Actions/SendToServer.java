package uk.co.nikodem.dFProxyPlugin.Player.Actions;

import com.velocitypowered.api.proxy.ConnectionRequestBuilder;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;

import java.util.Optional;

public class SendToServer {
    public static boolean sendPlayerToServer(Player plr, String server) {
        Optional<RegisteredServer> lobbyMaybe = DFProxyPlugin.server.getServer(server);
        if (lobbyMaybe.isEmpty()) {
            return false;
        }

        RegisteredServer lobby = lobbyMaybe.get();
        ConnectionRequestBuilder request = plr.createConnectionRequest(lobby);

        request.fireAndForget();
        return true;
    }
}
