package uk.co.nikodem.dFProxyPlugin.BanManager;

import com.velocitypowered.api.proxy.Player;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

public class BannedPlayer {
    private UUID uuid;
    private String username;
    private long startTimestamp;
    private long endTimestamp; // note: -1 means permanent
    private String reason;
    private boolean lobbyPermission;

    public BannedPlayer(String username, UUID uuid, long startTimestamp, long endTimestamp, String reason, boolean lobbyPermission) {
        this.uuid = uuid;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.reason = reason;
        this.lobbyPermission = lobbyPermission;

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

    public boolean getLobbyPermission() {
        return this.lobbyPermission;
    }

    public static BannedPlayer createInformation(Player plr, long start, long end, String reason, boolean lobbyPermission) {
        return new BannedPlayer(
                plr.getUsername(),
                plr.getUniqueId(),
                start,
                end,
                reason,
                lobbyPermission
        );
    }

    public static BannedPlayer createInformation(Player plr, long end, String reason, boolean lobbyPermission) {
        return new BannedPlayer(
                plr.getUsername(),
                plr.getUniqueId(),
                new Date().getTime(),
                end,
                reason,
                lobbyPermission
        );
    }

    public static BannedPlayer createInformation(Player plr, long end, String reason) {
        return new BannedPlayer(
                plr.getUsername(),
                plr.getUniqueId(),
                new Date().getTime(),
                end,
                reason,
                true
        );
    }

    public static BannedPlayer createInformation(Player plr, long end) {
        return new BannedPlayer(
                plr.getUsername(),
                plr.getUniqueId(),
                new Date().getTime(),
                end,
                "No reason provided.",
                true
        );
    }

    public static BannedPlayer createInformation(Player plr) {
        return new BannedPlayer(
                plr.getUsername(),
                plr.getUniqueId(),
                new Date().getTime(),
                new Date().getTime()+ Duration.ofHours(24).toMillis(),
                "No reason provided.",
                true
        );
    }

    // UUID based

    public static BannedPlayer createInformation(String username, UUID uuid, long start, long end, String reason, boolean lobbyPermission) {
        return new BannedPlayer(
                username,
                uuid,
                start,
                end,
                reason,
                lobbyPermission
        );
    }

    public static BannedPlayer createInformation(String username, UUID uuid, long end, String reason, boolean lobbyPermission) {
        return new BannedPlayer(
                username,
                uuid,
                new Date().getTime(),
                end,
                reason,
                lobbyPermission
        );
    }

    public static BannedPlayer createInformation(String username, UUID uuid, long end, String reason) {
        return new BannedPlayer(
                username,
                uuid,
                new Date().getTime(),
                end,
                reason,
                true
        );
    }

    public static BannedPlayer createInformation(String username, UUID uuid, long end) {
        return new BannedPlayer(
                username,
                uuid,
                new Date().getTime(),
                end,
                "No reason provided.",
                true
        );
    }

    public static BannedPlayer createInformation(String username, UUID uuid) {
        return new BannedPlayer(
                username,
                uuid,
                new Date().getTime(),
                new Date().getTime()+ Duration.ofHours(24).toMillis(),
                "No reason provided.",
                true
        );
    }
}
