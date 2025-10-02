package uk.co.nikodem.dFProxyPlugin.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import uk.co.nikodem.dFProxyPlugin.BanManager.BannedPlayer;
import uk.co.nikodem.dFProxyPlugin.BanManager.TimeManager;

import java.util.ArrayList;
import java.util.List;

public class JoinMessage {
    public static void sendMessage(ParsedPlayerInformation info) {
        List<Component> messages = null;
        if (info.isBedrock()) messages = createBedrockMessages();
        else messages = createJavaMessages();

        for (Component msg : messages) {
            info.getPlayer().sendMessage(msg);
        }
    }

    public static List<Component> createBedrockMessages() {
        List<Component> result = new ArrayList<>();

        result.add(Component.text("Welcome to the server!")
                .color(TextColor.color(0x03989e)));
        result.add(Component.text("Please note that you're playing on Bedrock, which is supported but doesn't work as well as Java.")
                .color(TextColor.color(0xB2482D)));
        result.add(Component.text("If you encounter any issues, or want to stay up to date, join the discord server!")
                .color(TextColor.color(0xB22D23)));
        result.add(Component.text("You can find the discord server at https://discord.gg/SpukTa6jBf")
                .color(TextColor.color(0xB22824)));

        return result;
    }

    public static List<Component> createJavaMessages() {
        List<Component> result = new ArrayList<>();

        result.add(Component.text("Welcome to the server!")
                .color(TextColor.color(0x03989e)));
        result.add(Component.text("You can join the discord server to keep updated with the server's updates!")
                .color(TextColor.color(0x5d782e)));
        result.add(Component.text("You can find the discord server at https://discord.gg/SpukTa6jBf")
                .color(TextColor.color(0x588163)));

        return result;
    }

    public static List<Component> createBanMessage(BannedPlayer info) {
        String reason = info.getReason();
        long start = info.getStart();
        long end = info.getEnd();

        List<Component> result = new ArrayList<>();

        String endTime = TimeManager.formatDuration(end - start);

        result.add(Component.text("You have been banned for "+endTime+"!")
                .color(TextColor.color(0xB22D23)));
        result.add(Component.text("Reason: "+reason+"!")
                .color(TextColor.color(0xB22D23)));
        result.add(Component.text("You can find the discord server at https://discord.gg/SpukTa6jBf")
                .color(TextColor.color(0x588163)));

        return result;
    }
}
