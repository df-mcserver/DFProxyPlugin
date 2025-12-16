package uk.co.nikodem.dFProxyPlugin.Discord.EventListeners;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.Component;

import net.kyori.adventure.text.minimessage.MiniMessage;
import uk.co.nikodem.dFProxyPlugin.Config.Config;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MessageInChannelListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getAuthor().isSystem()) return;

        for (Config.DiscordBot.BridgedChannel bridgedChannelInfo : getBridgedChannels(event.getChannel())) {
            if (bridgedChannelInfo == null) continue;

            Optional<RegisteredServer> serverPredicate = DFProxyPlugin.server.getServer(bridgedChannelInfo.getRegisteredServerName());
            serverPredicate.ifPresentOrElse((server -> {
                Component mcMessage = MiniMessage.miniMessage().deserialize(
                        String.format("<hover:show_text:'Sent by \"#%s\", otherwise known as \"%s\", on Discord<br>The message may be strangely formatted due to the differences between these platforms.'><dark_purple>[%s]</dark_purple> ",
                                event.getAuthor().getName(), event.getAuthor().getEffectiveName(), event.getAuthor().getEffectiveName())
                ).append(Component.text(event.getMessage().getContentRaw().replaceAll("\n", " ")));
                server.getPlayersConnected().forEach(plr -> plr.sendMessage(mcMessage));
            }), () -> {
                DFProxyPlugin.logger.warn("User #{} sent a message in #{} but there was no corresponding discord channel!", event.getAuthor().getName(), event.getChannel().getName());
            });
        }
    }

    public List<Config.DiscordBot.BridgedChannel> getBridgedChannels(MessageChannelUnion channel) {
        List<Config.DiscordBot.BridgedChannel> channels = new ArrayList<>();
        for (Config.DiscordBot.BridgedChannel bridgedChannel : DFProxyPlugin.config.discord_bot.getBridgedChannels()) {
            if (Objects.equals(bridgedChannel.getChannelId(), channel.getId())) channels.add(bridgedChannel);
        }
        return channels;
    }
}
