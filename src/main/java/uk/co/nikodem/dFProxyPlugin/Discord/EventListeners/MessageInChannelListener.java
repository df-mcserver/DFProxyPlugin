package uk.co.nikodem.dFProxyPlugin.Discord.EventListeners;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.Component;

import net.kyori.adventure.text.minimessage.MiniMessage;
import uk.co.nikodem.dFProxyPlugin.Config.Config;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;

import javax.annotation.Nonnull;
import java.util.Optional;

import static uk.co.nikodem.dFProxyPlugin.Discord.Utils.BridgedChannelsHelper.getBridgedChannels;

public class MessageInChannelListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getAuthor().isSystem()) return;
        if (event.getMessage().getContentRaw().isBlank()) return;

        for (Config.DiscordBot.BridgedChannel bridgedChannelInfo : getBridgedChannels(event.getChannel())) {
            if (bridgedChannelInfo == null) continue;

            Optional<RegisteredServer> serverPredicate = DFProxyPlugin.server.getServer(bridgedChannelInfo.getRegisteredServerName());
            serverPredicate.ifPresentOrElse((server -> {
                Component mcMessage = MiniMessage.miniMessage().deserialize(
                        String.format("<hover:show_text:'Sent by \"#%s\", otherwise known as \"%s\", on Discord<br>The message may be strangely formatted due to the differences between these platforms.<br>Certain things like message edits, and attachments cannot be seen via Minecraft.'><dark_purple>[%s]</dark_purple> ",
                                event.getAuthor().getName(), event.getAuthor().getEffectiveName(), event.getAuthor().getEffectiveName())
                ).append(Component.text(replaceEmojisWithEquivalents(event.getMessage().getContentRaw()).replaceAll("\n", " ")));
                server.getPlayersConnected().forEach(plr -> plr.sendMessage(mcMessage));
            }), () -> DFProxyPlugin.logger.warn("User #{} sent a message in #{} but there was no corresponding discord channel!", event.getAuthor().getName(), event.getChannel().getName()));
        }
    }

    public String replaceEmojisWithEquivalents(String msg) {
        msg = msg.replace("<:cold_sunglasses:1320410427863334965>", "\uE901");
        msg = msg.replace("\uD83D\uDE2D", "\uE902");
        msg = msg.replace("\uD83E\uDE91", "\uE903");
        msg = msg.replace("\uD83D\uDE0E", "\uE904");
        msg = msg.replace("\uD83D\uDE04", "\uE905");
        msg = msg.replace("\uD83E\uDE91", "\uE906");
        msg = msg.replace("\uD83D\uDE4F", "\uE907");
        msg = msg.replace("\uD83E\uDD70", "\uE908");
        msg = msg.replace("\uD83D\uDE3C", "\uE909");
        msg = msg.replace("\uD83E\uDD76", "\uE90A");
        msg = msg.replace("\uD83D\uDDE3️", "\uE90B");
        msg = msg.replace("\uD83D\uDD25", "\uE90C");
        msg = msg.replace("\uD83D\uDC1F", "\uE90D");
        msg = msg.replace("\uD83D\uDDFF", "\uE90E");
        msg = msg.replace("<:thumbs:1320855387884355707>", "\uE90F");
        msg = msg.replace("<:salute:1320855385476960266>", "\uE910");
        msg = msg.replace("<:shrug:1320855386710081546>", "\uE911");
        msg = msg.replace("<:wompwomp:1320855389251834046>", "\uE912");
        msg = msg.replace("<:joobi:1323391120134377503>", "\uE913");
        msg = msg.replace("\uD83D\uDC4B", "\uE914");
        msg = msg.replace("\uD83D\uDE14", "\uE915");
        msg = msg.replace("\uD83D\uDE31", "\uE916");
        msg = msg.replace("\uD83D\uDE08", "\uE917");
        msg = msg.replace("\uD83D\uDE10", "\uE918");
        msg = msg.replace("\uD83D\uDE42", "\uE919");
        msg = msg.replace("\uD83D\uDE33", "\uE91A");
        msg = msg.replace("\uD83D\uDE32", "\uE91B");
        msg = msg.replace("\uD83E\uDD2E", "\uE91C");
        msg = msg.replace("\uD83D\uDE40", "\uE91D");
        msg = msg.replace("\uD83E\uDD7A", "\uE91E");
        msg = msg.replace("\uD83D\uDE20", "\uE91F");
        msg = msg.replace("\uD83D\uDE21", "\uE920");
        msg = msg.replace("\uD83E\uDD13", "\uE921");
        msg = msg.replace("\uD83D\uDD30", "\uE922");

        msg = msg.replace("\uD83E\uDD40", "\uE923");
        msg = msg.replace("\uD83C\uDF8B", "\uE924");
        msg = msg.replace("\u2764\uFE0F", "\uE925");
        msg = msg.replace("\uD83D\uDC94", "\uE926");
        msg = msg.replace("\uD83C\uDFF3\uFE0F\u200D⚧\uFE0F", "\uE927");
        msg = msg.replace("\uD83D\uDD0B", "\uE928");
        msg = msg.replace("\uD83E\uDEAB", "\uE929");
        msg = msg.replace("❤\uFE0F\u200D\uD83E\uDE79", "\uE92A");
        msg = msg.replace("\uD83C\uDF39", "\uE92B");
        msg = msg.replace("\uD83D\uDDA4", "\uE92C");
        msg = msg.replace("\uD83E\uDD14", "\uE92D");
        msg = msg.replace("\uD83D\uDE15", "\uE92E");
        msg = msg.replace("\uD83D\uDC4D", "\uE92F");
        msg = msg.replace("\uD83D\uDC4E", "\uE930");
        msg = msg.replace("\uD83C\uDF89", "\uE931");
        msg = msg.replace("\uD83E\uDEF6", "\uE932");
        msg = msg.replace("\uD83E\uDEE9", "\uE933");
        msg = msg.replace("❌", "\uE934");
        msg = msg.replace("✅", "\uE935");
        msg = msg.replace("\uD83E\uDD73", "\uE936");
        msg = msg.replace("<:false:1563275403659968584>", "\uE937");
        msg = msg.replace("<:true:1563275776764157982>", "\uE938");
        msg = msg.replace("<:thumbs_up:1563275449579077633>", "\uE939");
        msg = msg.replace("<:fucyoy:1563275743897583706>", "\uE93A");
        msg = msg.replace("<:silly:1563275432076386324>", "\uE93B");
        msg = msg.replace("<:mrrp:1563275420567076924>", "\uE93C");
        msg = msg.replace("<:cat_sit:1563275377659355366>", "\uE93D");
        msg = msg.replace("<:steamhappy:1563276184710553741>", "\uE93E");

        return msg;
    }
}
