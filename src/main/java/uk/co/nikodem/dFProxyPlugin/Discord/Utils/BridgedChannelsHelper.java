package uk.co.nikodem.dFProxyPlugin.Discord.Utils;

import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import uk.co.nikodem.dFProxyPlugin.Config.Config;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BridgedChannelsHelper {
    public static List<Config.DiscordBot.BridgedChannel> getBridgedChannels(String serverName) {
        List<Config.DiscordBot.BridgedChannel> channels = new ArrayList<>();
        for (Config.DiscordBot.BridgedChannel bridgedChannel : DFProxyPlugin.config.discord_bot.getBridgedChannels()) {
            if (Objects.equals(bridgedChannel.getRegisteredServerName(), serverName)) channels.add(bridgedChannel);
        }
        return channels;
    }

    public static List<Config.DiscordBot.BridgedChannel> getBridgedChannels(MessageChannelUnion channel) {
        List<Config.DiscordBot.BridgedChannel> channels = new ArrayList<>();
        for (Config.DiscordBot.BridgedChannel bridgedChannel : DFProxyPlugin.config.discord_bot.getBridgedChannels()) {
            if (Objects.equals(bridgedChannel.getChannelId(), channel.getId())) channels.add(bridgedChannel);
        }
        return channels;
    }
}
