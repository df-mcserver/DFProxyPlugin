package uk.co.nikodem.dFProxyPlugin.Bans;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Date;
import java.util.UUID;

public class BanInformation {
    private final UUID uuid;
    private final long startTimestamp;
    private final long endTimestamp; // note: <0 means permanent
    private final String reason;

    public BanInformation(UUID uuid, long startTimestamp, long endTimestamp, String reason) {
        this.uuid = uuid;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.reason = reason;
    }

    public long getStart() {
        return this.startTimestamp;
    }

    public long getEnd() {
        return this.endTimestamp;
    }

    public String getReason() {
        return this.reason;
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    public boolean isPermanentlyBanned() {
        return this.endTimestamp < 0;
    }

    public Component getBanMessage() {
        String endTime = TimeManager.formatDuration(this.endTimestamp - this.startTimestamp);
        return MiniMessage.miniMessage().deserialize("<red>You have been"+ (this.isPermanentlyBanned() ? " permanently" : "") + " banned from this server!<br><br>Reason:</red> "+this.reason+"<reset><br><br><red>You will be unbanned "+endTime);
    }

    public static BanInformation createInformation(Player plr, long start, long end, String reason) {
        return new BanInformation(
                plr.getUniqueId(),
                start,
                end,
                reason
        );
    }

    public static BanInformation createInformation(Player plr, long end, String reason) {
        return new BanInformation(
                plr.getUniqueId(),
                new Date().getTime(),
                end,
                reason
        );
    }

    public static BanInformation createInformation(Player plr, long end) {
        return new BanInformation(
                plr.getUniqueId(),
                new Date().getTime(),
                end,
                "No reason provided."
        );
    }

    public static BanInformation createInformation(Player plr) {
        return new BanInformation(
                plr.getUniqueId(),
                new Date().getTime(),
                -1,
                "No reason provided."
        );
    }

    // UUID based

    public static BanInformation createInformation(UUID uuid, long start, long end, String reason) {
        return new BanInformation(
                uuid,
                start,
                end,
                reason
        );
    }

    public static BanInformation createInformation(UUID uuid, long end, String reason) {
        return new BanInformation(
                uuid,
                new Date().getTime(),
                end,
                reason
        );
    }

    public static BanInformation createInformation(UUID uuid, long end) {
        return new BanInformation(
                uuid,
                new Date().getTime(),
                end,
                "No reason provided."
        );
    }

    public static BanInformation createInformation(UUID uuid) {
        return new BanInformation(
                uuid,
                new Date().getTime(),
                -1,
                "No reason provided."
        );
    }
}
