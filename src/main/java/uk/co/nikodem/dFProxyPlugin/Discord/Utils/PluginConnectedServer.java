package uk.co.nikodem.dFProxyPlugin.Discord.Utils;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import uk.co.nikodem.dFProxyPlugin.Config.Config;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;

import java.util.ArrayList;
import java.util.List;

import static uk.co.nikodem.dFProxyPlugin.Discord.Utils.BridgedChannelsHelper.getBridgedChannels;

public class PluginConnectedServer {
    public static List<String> serverNamesConnected = new ArrayList<>();

    public static boolean registerServer(RegisteredServer server) {
        if (!DFProxyPlugin.config.discord_bot.isEnabled()) return false;
        String name = server.getServerInfo().getName();

        List<Config.DiscordBot.BridgedChannel> bridgedChannels = getBridgedChannels(name);
        if (bridgedChannels.isEmpty()) return false;

        if (!serverNamesConnected.contains(name)) {
            serverNamesConnected.add(name);
        }

        return true;
    }

    public static boolean isServerRegistered(RegisteredServer server) {
        return serverNamesConnected.contains(server.getServerInfo().getName());
    }
}
