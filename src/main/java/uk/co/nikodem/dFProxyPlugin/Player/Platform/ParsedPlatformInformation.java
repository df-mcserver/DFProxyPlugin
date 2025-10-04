package uk.co.nikodem.dFProxyPlugin.Player.Platform;

import com.velocitypowered.api.proxy.Player;
import org.geysermc.geyser.api.connection.GeyserConnection;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;
import uk.co.nikodem.dFProxyPlugin.Player.Platform.Versions.BedrockPlatformInformation;
import uk.co.nikodem.dFProxyPlugin.Player.Platform.Versions.JavaPlatformInformation;

import javax.annotation.Nullable;
import java.util.*;

public interface ParsedPlatformInformation {
    @Nullable
    public Player getPlayer();
    public UUID getUniqueId();
    public String getServerUsername();
    public String getRealUsername();
    public String getPlatformName();
    public String getMinecraftPlatformName();
    public String getDevicePlatformName();
    public int getProtocolVersion();
    public String getMinecraftVersion();
    @Nullable public GeyserConnection getGeyserConnection();
    public Boolean isBedrock();
    public default Boolean isJava() {
        return !isBedrock();
    }
    @Nullable public String getClientBrandName();

    public Boolean isModded();

    public default boolean isIncompatible() {
        if (getClientBrandName() == null) return true;
        List<String> allowedClients = List.of("vanilla", "fabric", "neoforge", "quilt");
        return !allowedClients.contains(getClientBrandName());
    }

    // caching
    public static Map<UUID, ParsedPlatformInformation> cachedPlayers = new HashMap<>();

    @Nullable
    public static ParsedPlatformInformation getCachedParsedPlayerInformation(UUID uuid) {
        return cachedPlayers.get(uuid);
    }

    public static void removePlayerFromCache(UUID uuid) {
        cachedPlayers.remove(uuid);
    }

    public static ParsedPlatformInformation fromUUID(UUID uuid) {
        // check if cached exists
        ParsedPlatformInformation cachedResult = getCachedParsedPlayerInformation(uuid);
        if (cachedResult != null) return cachedResult;

        // create and add to cache
        ParsedPlatformInformation newResult =
                DFProxyPlugin.geyser.isBedrockPlayer(uuid) ?
                        new BedrockPlatformInformation(uuid) : new JavaPlatformInformation(uuid);

        cachedPlayers.put(uuid, newResult);
        return newResult;
    }

    public static ParsedPlatformInformation fromPlayer(Player plr) {
        return fromUUID(plr.getUniqueId());
    }
}
