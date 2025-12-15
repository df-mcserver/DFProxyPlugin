package uk.co.nikodem.dFProxyPlugin.Bans;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Date;
import java.util.UUID;

public class BannedPlayer {
    private final UUID uuid;
    private final long startTimestamp;
    private final long endTimestamp; // note: <0 means permanent
    private final String reason;

    public BannedPlayer(UUID uuid, long startTimestamp, long endTimestamp, String reason) {
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
        return Component.text(
                "You have been"+ (this.isPermanentlyBanned() ? " permanently" : "") + " banned from this server!\n\nReason: ", NamedTextColor.RED
        ).append(
                Component.text(this.reason, NamedTextColor.WHITE)
        ).append(
                (this.isPermanentlyBanned() ?
                        Component.newline()
                        : Component.text("\n\nYou will be unbanned "+endTime, NamedTextColor.RED)
                )
        );
    }

    public static BannedPlayer createInformation(Player plr, long start, long end, String reason) {
        return new BannedPlayer(
                plr.getUniqueId(),
                start,
                end,
                reason
        );
    }

    public static BannedPlayer createInformation(Player plr, long end, String reason) {
        return new BannedPlayer(
                plr.getUniqueId(),
                new Date().getTime(),
                end,
                reason
        );
    }

    public static BannedPlayer createInformation(Player plr, long end) {
        return new BannedPlayer(
                plr.getUniqueId(),
                new Date().getTime(),
                end,
                "No reason provided."
        );
    }

    public static BannedPlayer createInformation(Player plr) {
        return new BannedPlayer(
                plr.getUniqueId(),
                new Date().getTime(),
                -1,
                "No reason provided."
        );
    }

    // UUID based

    public static BannedPlayer createInformation(UUID uuid, long start, long end, String reason, boolean lobbyPermission) {
        return new BannedPlayer(
                uuid,
                start,
                end,
                reason
        );
    }

    public static BannedPlayer createInformation(UUID uuid, long end, String reason, boolean lobbyPermission) {
        return new BannedPlayer(
                uuid,
                new Date().getTime(),
                end,
                reason
        );
    }

    public static BannedPlayer createInformation(UUID uuid, long end, String reason) {
        return new BannedPlayer(
                uuid,
                new Date().getTime(),
                end,
                reason
        );
    }

    public static BannedPlayer createInformation(UUID uuid, long end) {
        return new BannedPlayer(
                uuid,
                new Date().getTime(),
                end,
                "No reason provided."
        );
    }

    public static BannedPlayer createInformation(UUID uuid) {
        return new BannedPlayer(
                uuid,
                new Date().getTime(),
                -1,
                "No reason provided."
        );
    }
}
