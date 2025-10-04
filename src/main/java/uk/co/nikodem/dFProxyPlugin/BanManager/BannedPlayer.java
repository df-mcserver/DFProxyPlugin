package uk.co.nikodem.dFProxyPlugin.BanManager;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.awt.*;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

public class BannedPlayer {
    private UUID uuid;
    private String username;
    private long startTimestamp;
    private long endTimestamp; // note: -1 means permanent
    private String reason;

    public BannedPlayer(String username, UUID uuid, long startTimestamp, long endTimestamp, String reason) {
        this.uuid = uuid;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.reason = reason;

        this.username = username;
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

    public String getUsername() {
        return this.username;
    }

    public boolean isPernamentlyBanned() {
        return this.endTimestamp < 0;
    }

    public Component getBanMessage() {
        return Component.text(
                "You have been "+ (this.isPernamentlyBanned() ? "permanently" : "") + " banned from this server!\n\nReason: ", NamedTextColor.RED
        ).append(
                Component.text(this.reason, NamedTextColor.WHITE)
        ).append(
                (this.isPernamentlyBanned() ?
                        Component.newline()
                        : Component.text("\n\nYou will be unbanned on "+new Date(this.endTimestamp), NamedTextColor.RED)
                )
        );
    }

    public static BannedPlayer createInformation(Player plr, long start, long end, String reason) {
        return new BannedPlayer(
                plr.getUsername(),
                plr.getUniqueId(),
                start,
                end,
                reason
        );
    }

    public static BannedPlayer createInformation(Player plr, long end, String reason) {
        return new BannedPlayer(
                plr.getUsername(),
                plr.getUniqueId(),
                new Date().getTime(),
                end,
                reason
        );
    }

    public static BannedPlayer createInformation(Player plr, long end) {
        return new BannedPlayer(
                plr.getUsername(),
                plr.getUniqueId(),
                new Date().getTime(),
                end,
                "No reason provided."
        );
    }

    public static BannedPlayer createInformation(Player plr) {
        return new BannedPlayer(
                plr.getUsername(),
                plr.getUniqueId(),
                new Date().getTime(),
                new Date().getTime()+ Duration.ofHours(24).toMillis(),
                "No reason provided."
        );
    }

    // UUID based

    public static BannedPlayer createInformation(String username, UUID uuid, long start, long end, String reason, boolean lobbyPermission) {
        return new BannedPlayer(
                username,
                uuid,
                start,
                end,
                reason
        );
    }

    public static BannedPlayer createInformation(String username, UUID uuid, long end, String reason, boolean lobbyPermission) {
        return new BannedPlayer(
                username,
                uuid,
                new Date().getTime(),
                end,
                reason
        );
    }

    public static BannedPlayer createInformation(String username, UUID uuid, long end, String reason) {
        return new BannedPlayer(
                username,
                uuid,
                new Date().getTime(),
                end,
                reason
        );
    }

    public static BannedPlayer createInformation(String username, UUID uuid, long end) {
        return new BannedPlayer(
                username,
                uuid,
                new Date().getTime(),
                end,
                "No reason provided."
        );
    }

    public static BannedPlayer createInformation(String username, UUID uuid) {
        return new BannedPlayer(
                username,
                uuid,
                new Date().getTime(),
                new Date().getTime()+ Duration.ofHours(24).toMillis(),
                "No reason provided."
        );
    }
}
