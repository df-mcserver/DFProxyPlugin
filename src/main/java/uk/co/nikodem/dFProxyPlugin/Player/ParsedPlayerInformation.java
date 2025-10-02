package uk.co.nikodem.dFProxyPlugin.Player;

import com.velocitypowered.api.proxy.Player;
import org.geysermc.geyser.api.connection.GeyserConnection;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;
import uk.co.nikodem.dFProxyPlugin.Player.Versions.BedrockPlayerInformation;
import uk.co.nikodem.dFProxyPlugin.Player.Versions.JavaPlayerInformation;

import javax.annotation.Nullable;
import java.util.*;

public interface ParsedPlayerInformation {
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
    @Nullable
    public GeyserConnection getGeyserConnection();
    public Boolean isBedrock();
    public default Boolean isJava() {
        return !isBedrock();
    }
    public String getClientBrandName();
    public List<String> getMods();
    public Boolean isModded();

    // caching
    public static Map<UUID, ParsedPlayerInformation> cachedPlayers = new HashMap<>();

    @Nullable
    public static ParsedPlayerInformation getCachedParsedPlayerInformation(UUID uuid) {
        return cachedPlayers.get(uuid);
    }

    public static void removePlayerFromCache(UUID uuid) {
        cachedPlayers.remove(uuid);
    }

    public static ParsedPlayerInformation fromUUID(UUID uuid) {
        // check if cached exists
        ParsedPlayerInformation cachedResult = getCachedParsedPlayerInformation(uuid);
        if (cachedResult != null) return cachedResult;

        // create and add to cache
        ParsedPlayerInformation newResult =
                DFProxyPlugin.geyser.isBedrockPlayer(uuid) ?
                        new BedrockPlayerInformation(uuid) : new JavaPlayerInformation(uuid);

        cachedPlayers.put(uuid, newResult);
        return newResult;
    }

    public static ParsedPlayerInformation fromPlayer(Player plr) {
        return fromUUID(plr.getUniqueId());
    }
}
