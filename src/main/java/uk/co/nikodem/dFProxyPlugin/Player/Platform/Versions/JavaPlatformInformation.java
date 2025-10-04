package uk.co.nikodem.dFProxyPlugin.Player.Platform.Versions;

import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.proxy.Player;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.jetbrains.annotations.Nullable;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;
import uk.co.nikodem.dFProxyPlugin.Player.Platform.ParsedPlatformInformation;

import java.util.*;

public class JavaPlatformInformation implements ParsedPlatformInformation {
    private final UUID uuid;
    private final int protocolVersion;
    private final String minecraftVersion;
    private final String username;
    private final String brandName;

    public JavaPlatformInformation(UUID uuid) {
        this.uuid = uuid;
        this.protocolVersion = getPlayer().getProtocolVersion().getProtocol();

        String rawVersion = "MINECRAFT_Unknown";

        for (var version : ProtocolVersion.ID_TO_PROTOCOL_CONSTANT.entrySet()) {
            int id = version.getKey();
            if (this.protocolVersion == id) {
                rawVersion = version.getValue().name();
                break;
            }
        }
        rawVersion = rawVersion.replace("MINECRAFT_", "");
        rawVersion = rawVersion.replace("_", ".");
        this.minecraftVersion = rawVersion;


        Player plr = getPlayer();
        this.username = plr == null ? "Unknown" : getPlayer().getUsername();
        this.brandName = plr == null ? "Unknown" : plr.getClientBrand();
    }

    public JavaPlatformInformation(String uuid, int protocolVersion, String minecraftVersion, String username, String brandName) {
        this.uuid = UUID.fromString(uuid);
        this.protocolVersion = protocolVersion;
        this.minecraftVersion = minecraftVersion;
        this.username = username;
        this.brandName = brandName;
    }

    @Override
    public String getServerUsername() {
        return this.username;
    }

    @Override
    public String getRealUsername() {
        return this.username;
    }

    @Nullable
    @Override
    public Player getPlayer() {
        Optional<Player> plrmaybe = DFProxyPlugin.server.getPlayer(uuid);
        return plrmaybe.orElse(null);
    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public String getPlatformName() {
        return getMinecraftPlatformName();
    }

    @Override
    public String getMinecraftPlatformName() {
        return "Java";
    }

    @Override
    public String getDevicePlatformName() {
        return "Unknown";
    }

    @Override
    public int getProtocolVersion() {
        return getPlayer().getProtocolVersion().getProtocol();
    }

    @Override
    public String getMinecraftVersion() {
        return minecraftVersion;
    }

    @Override
    public @Nullable GeyserConnection getGeyserConnection() {
        return null;
    }

    @Override
    public Boolean isBedrock() {
        return false;
    }

    @Override
    public String getClientBrandName() {
        return this.brandName;
    }

    @Override
    public Boolean isModded() {
        // obviously not foolproof, but good enough
        return !Objects.equals(this.brandName, "vanilla");
    }
}
