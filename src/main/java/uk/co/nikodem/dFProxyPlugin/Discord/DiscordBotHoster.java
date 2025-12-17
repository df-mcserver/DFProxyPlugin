package uk.co.nikodem.dFProxyPlugin.Discord;

import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;
import uk.co.nikodem.dFProxyPlugin.Discord.EventListeners.MessageInChannelListener;
import uk.co.nikodem.dFProxyPlugin.Discord.EventListeners.MessageInMinecraftHandler;
import uk.co.nikodem.dFProxyPlugin.Discord.EventListeners.PlayerEventsInMinecraftHandler;

import java.util.EnumSet;

public class DiscordBotHoster {
    public DiscordThread thread;

    public DiscordBotHoster() {
        thread = new DiscordThread();
        thread.start();
    }

    public static class DiscordThread extends Thread {
        public JDA jda;
        public MessageInMinecraftHandler messageInMinecraftHandler;
        public PlayerEventsInMinecraftHandler playerEventsInMinecraftHandler;

        public final EnumSet<GatewayIntent> intents = EnumSet.of(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT
        );

        public JDA getJda() {
            return jda;
        }

        @Override
        public void run() {
            messageInMinecraftHandler = new MessageInMinecraftHandler();
            playerEventsInMinecraftHandler = new PlayerEventsInMinecraftHandler();

            jda = JDABuilder.createLight(DFProxyPlugin.config.discord_bot.getToken(), intents)
                    .addEventListeners(new MessageInChannelListener())
                    .setActivity(Activity.customStatus(DFProxyPlugin.config.discord_bot.getCustomStatus()))
                    .build();

            jda.getRestPing()
                    .queue(ping -> DFProxyPlugin.logger.info("Logged into discord with {} ping!", ping));

            try {
                jda.awaitReady();
            } catch (InterruptedException e) {
                DFProxyPlugin.logger.warn("Discord thread encountered an error setting up JDA!");
                throw new RuntimeException(e);
            }
        }

        public void onPlayerChat(PlayerChatEvent event) {
            messageInMinecraftHandler.onPlayerChat(event, jda);
        }

        public void onPluginDiscordStandardMessage(Player plr, RegisteredServer server, String msg) {
            messageInMinecraftHandler.onPluginDiscordStandardMessage(plr, server, msg, jda);
        }

        public void onPlayerConnectToServer(ServerPostConnectEvent event) {
            playerEventsInMinecraftHandler.onPlayerConnectToServer(event, jda);
        }

        public void onDisconnectFromProxy(DisconnectEvent event) {
            playerEventsInMinecraftHandler.onDisconnectFromProxy(event, jda);
        }
    }
}
