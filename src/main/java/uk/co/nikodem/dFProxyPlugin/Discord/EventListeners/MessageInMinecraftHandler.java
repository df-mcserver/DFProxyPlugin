package uk.co.nikodem.dFProxyPlugin.Discord.EventListeners;

import club.minnced.discord.webhook.external.JDAWebhookClient;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import uk.co.nikodem.dFProxyPlugin.Config.Config;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;
import uk.co.nikodem.dFProxyPlugin.Discord.Utils.MCAvatarURLHelper;
import uk.co.nikodem.dFProxyPlugin.Discord.Utils.PluginConnectedServer;

import java.util.HashMap;

import static uk.co.nikodem.dFProxyPlugin.Discord.Utils.BridgedChannelsHelper.getBridgedChannels;

public class MessageInMinecraftHandler {
    public HashMap<String, JDAWebhookClient> clientMappings = new HashMap<>();

    public void onPlayerChat(PlayerChatEvent event, JDA jda) {
        Player plr = event.getPlayer();
        if (plr.getCurrentServer().isEmpty()) return;

        RegisteredServer server = plr.getCurrentServer().get().getServer();

        if (PluginConnectedServer.isServerRegistered(server)) return;

        for (Config.DiscordBot.BridgedChannel bridgedChannelInfo : getBridgedChannels(server.getServerInfo().getName())) {
            if (bridgedChannelInfo == null) continue;

            GuildChannel guildChannel = jda.getChannelById(GuildChannel.class, bridgedChannelInfo.getChannelId());
            if (guildChannel == null) {
                DFProxyPlugin.logger.warn("Player {} sent a message in {}, but there was no corresponding discord channel!", plr.getUsername(), server.getServerInfo().getName());
                continue;
            }

            if (guildChannel instanceof MessageChannelUnion channel) {
                boolean fallback = false;

                if (bridgedChannelInfo.getWebhookUrl().isBlank()) fallback = true;
                else {
                    JDAWebhookClient client;
                    if (clientMappings.containsKey(bridgedChannelInfo.getChannelId())) {
                        client = clientMappings.get(bridgedChannelInfo.getChannelId());
                    } else {
                        client = JDAWebhookClient.withUrl(bridgedChannelInfo.getWebhookUrl());
                    }
                    if (client != null) {
                        WebhookMessage message = new WebhookMessageBuilder()
                                .setUsername(plr.getUsername()) // use this username
                                .setAvatarUrl(MCAvatarURLHelper.getAvatarURL(plr)) // use this avatar
                                .setContent(event.getMessage())
                                .build();

                        client.send(message);
                    } else fallback = true;
                }

                if (fallback) channel.sendMessage(String.format("<%s> %s", plr.getUsername(), event.getMessage())).queue();
            }
        }
    }
}
