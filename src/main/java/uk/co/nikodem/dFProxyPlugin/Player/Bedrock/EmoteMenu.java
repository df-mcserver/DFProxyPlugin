package uk.co.nikodem.dFProxyPlugin.Player.Bedrock;

import com.velocitypowered.api.proxy.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.geysermc.geyser.api.event.bedrock.ClientEmoteEvent;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class EmoteMenu {
    public static void handleEmote(ClientEmoteEvent event) {
        GeyserConnection connection = event.connection();
        event.setCancelled(true);
        connection.sendMessage("Emotes aren't supported on this server!");


//        SimpleForm.Builder form = SimpleForm.builder()
//                .title("Quick commands")
//                .content("Click on one of the commands below to run it.")
//                .button("/accessories")
//                .button("/lobby")
//                .button("/about")
//                .button("/bukkit:about")
//                .button("/minecraft:me")
//                .button("/me");
//
//        form.validResultHandler(((simpleForm, simpleFormResponse) -> {
//            String command = simpleFormResponse.clickedButton().text();
//
//            Optional<Player> plrMaybe = DFProxyPlugin.server.getPlayer(connection.javaUsername());
//            plrMaybe.ifPresent((player -> {
//                CompletableFuture<Boolean> commandResult = DFProxyPlugin.commandManager.executeImmediatelyAsync(player, command);
//
//                // TODO: fix commands
//
//                commandResult.whenComplete(((aBoolean, throwable) -> {
//                    System.out.println("COMMAND sent ~*!!(^Y78otweifyuragfksieurghlsertougyhal;u");
//                    System.out.println(aBoolean);
//                    throwable.printStackTrace();
//                }));
//            }));
//        }));
//
//        connection.sendForm(form.build());
    }
}
