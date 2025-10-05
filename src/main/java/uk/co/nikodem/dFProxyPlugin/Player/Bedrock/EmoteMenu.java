package uk.co.nikodem.dFProxyPlugin.Player.Bedrock;

import com.velocitypowered.api.proxy.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.geysermc.geyser.api.event.bedrock.ClientEmoteEvent;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;
import uk.co.nikodem.dFProxyPlugin.Player.Actions.SendToServer;

import java.util.Optional;

public class EmoteMenu {
    public static void handleEmote(ClientEmoteEvent event) {
        GeyserConnection connection = event.connection();
        event.setCancelled(true);

        SimpleForm.Builder form = SimpleForm.builder()
                .title("Quick actions")
                .button("Return to lobby");

        form.validResultHandler(((simpleForm, simpleFormResponse) -> {
            String buttonPressed = simpleFormResponse.clickedButton().text();
            Optional<Player> plrMaybe = DFProxyPlugin.server.getPlayer(connection.javaUsername());
            plrMaybe.ifPresent((player -> {
                if (buttonPressed.equals("Return to lobby")) {
                    SendToServer.sendPlayerToServer(player, "lobby");
                }
            }));
        }));

        connection.sendForm(form.build());
    }
}
