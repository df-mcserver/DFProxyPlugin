package uk.co.nikodem.dFProxyPlugin.Discord.EventListeners;

import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import uk.co.nikodem.dFProxyPlugin.Config.Config;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;
import uk.co.nikodem.dFProxyPlugin.Discord.Utils.MCAvatarURLHelper;

import java.awt.*;

import static uk.co.nikodem.dFProxyPlugin.Discord.Utils.BridgedChannelsHelper.getBridgedChannels;

public class PlayerEventsInMinecraftHandler {
    public void onPlayerConnectToServer(ServerPostConnectEvent event, JDA jda) {
        if (event.getPlayer().getCurrentServer().isEmpty()) return;
        Player plr = event.getPlayer();
        String avatarURL = MCAvatarURLHelper.getAvatarURL(plr);
        String username = plr.getUsername();
        String serverName = event.getPlayer().getCurrentServer().get().getServerInfo().getName();

        RegisteredServer previousServer = event.getPreviousServer();
        if (previousServer != null) {
            String previousServerName = previousServer.getServerInfo().getName();
            doEmbedToBridgedChannels(jda, previousServerName, username, avatarURL, "left", Color.RED);
        }

        doEmbedToBridgedChannels(jda, serverName, username, avatarURL, "joined", Color.GREEN);
    }

    public void onDisconnectFromProxy(DisconnectEvent event, JDA jda) {
        if (event.getPlayer().getCurrentServer().isEmpty()) return;
        Player plr = event.getPlayer();
        String avatarURL = MCAvatarURLHelper.getAvatarURL(plr);
        String username = plr.getUsername();
        String serverName = event.getPlayer().getCurrentServer().get().getServerInfo().getName();

        doEmbedToBridgedChannels(jda, serverName, username, avatarURL, "left", Color.RED);
    }

    public void doEmbedToBridgedChannels(JDA jda, String serverName, String username, String avatarURL, String action, Color colour) {
        for (Config.DiscordBot.BridgedChannel bridgedChannelInfo : getBridgedChannels(serverName)) {
            if (bridgedChannelInfo == null) continue;

            GuildChannel guildChannel = jda.getChannelById(GuildChannel.class, bridgedChannelInfo.getChannelId());
            if (guildChannel == null) {
                DFProxyPlugin.logger.warn("Player {} {} {}, but there was no corresponding discord channel!", username, action, serverName);
                continue;
            }
            if (guildChannel instanceof MessageChannelUnion channel) {
                channel.sendMessageEmbeds(
                        new EmbedBuilder()
                                .setAuthor(username, null, avatarURL)
                                .setColor(colour)
                                .setDescription(username+" has "+action+" the server")
                                .build()
                ).queue();
            }
        }
    }
}
