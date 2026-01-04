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
import uk.co.nikodem.dFProxyPlugin.Discord.Utils.PluginConnectedServer;

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

            if (previousServer.getPlayersConnected().isEmpty()) PluginConnectedServer.unregisterServer(previousServer);
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

        RegisteredServer server = event.getPlayer().getCurrentServer().get().getServer();
        if (server.getPlayersConnected().isEmpty()) PluginConnectedServer.unregisterServer(server);
    }

    public void doEmbedToBridgedChannels(JDA jda, String serverName, String username, String avatarURL, String action, Color colour) {
        doEmbed(jda, serverName, username, avatarURL, username+" has "+action+" the server", colour);
    }

    public void doEmbed(JDA jda, String serverName, String username, String avatarURL, String message, Color colour) {
        for (Config.DiscordBot.BridgedChannel bridgedChannelInfo : getBridgedChannels(serverName)) {
            if (bridgedChannelInfo == null) continue;

            GuildChannel guildChannel = jda.getChannelById(GuildChannel.class, bridgedChannelInfo.getChannelId());
            if (guildChannel == null) {
                DFProxyPlugin.logger.warn("{} tried to send embed from {}, but there was no corresponding discord channel!", username, serverName);
                continue;
            }
            if (guildChannel instanceof MessageChannelUnion channel) {
                channel.sendMessageEmbeds(
                        new EmbedBuilder()
                                .setAuthor(username, null, avatarURL)
                                .setColor(colour)
                                .setDescription(message)
                                .build()
                ).queue();
            }
        }
    }
}
