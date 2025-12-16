package uk.co.nikodem.dFProxyPlugin.Discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;
import uk.co.nikodem.dFProxyPlugin.Discord.EventListeners.MessageInChannelListener;

import java.util.EnumSet;

public class DiscordBotHoster {
    public DiscordBotHoster() {
        DiscordThread thread = new DiscordThread();
        thread.start();
    }

    public static class DiscordThread extends Thread {
        public JDA jda;

        public final EnumSet<GatewayIntent> intents = EnumSet.of(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT
        );

        @Override
        public void run() {
            jda = JDABuilder.createLight(DFProxyPlugin.config.discord_bot.getToken(), intents)
                    .addEventListeners(new MessageInChannelListener())
                    .setActivity(Activity.customStatus(DFProxyPlugin.config.discord_bot.getCustomStatus()))
                    .build();

            jda.getRestPing()
                    .queue(ping -> DFProxyPlugin.logger.info("Logged in with ping: {}", ping));

            try {
                jda.awaitReady();
            } catch (InterruptedException e) {
                DFProxyPlugin.logger.warn("Discord thread encountered an error setting up JDA!");
                throw new RuntimeException(e);
            }
        }
    }
}
