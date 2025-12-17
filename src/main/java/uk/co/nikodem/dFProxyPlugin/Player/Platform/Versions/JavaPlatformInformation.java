package uk.co.nikodem.dFProxyPlugin.Player.Platform.Versions;

import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.proxy.Player;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.jetbrains.annotations.Nullable;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;
import uk.co.nikodem.dFProxyPlugin.Player.Platform.ParsedPlatformInformation;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class JavaPlatformInformation implements ParsedPlatformInformation {
    private final UUID uuid;
    private final int protocolVersion;
    private final String minecraftVersion;
    private final String username;
    private final String brandName;

    public JavaPlatformInformation(UUID uuid) {
        this.uuid = uuid;

        Player plr = getPlayer();

        this.protocolVersion = DFProxyPlugin.viaAPI.getPlayerProtocolVersion(plr).getVersion();

        String first = ProtocolVersion.getProtocolVersion(protocolVersion).getVersionIntroducedIn();
        String last = ProtocolVersion.getProtocolVersion(protocolVersion).getMostRecentSupportedVersion();
        this.minecraftVersion = first.equals(last) ? first : first+"-"+last;

        this.username = plr == null ? "Unknown" : plr.getUsername();
        this.brandName = plr == null ? "Unknown" : plr.getClientBrand() == null ? "vanilla" : plr.getClientBrand();
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
        return this.protocolVersion;
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

    @Override
    public String toString() {
        return "====================\n" +
                MessageFormat.format("  - Username: {0}\n", this.getServerUsername().equals(this.getRealUsername()) ? this.getServerUsername() : this.getServerUsername()+" (aka "+this.getRealUsername()+")") +
                MessageFormat.format("  - UUID: {0}\n", this.getUniqueId().toString()) +
                "  - Client information:\n" +
                MessageFormat.format("    - Client: {0}{1}\n", this.getClientBrandName(), this.isModded() ? " (likely modded)" : "") +
                MessageFormat.format("    - Platform: {0}\n", this.getPlatformName()) +
                MessageFormat.format("    - Version: {0}\n",
                        this.getMinecraftVersion() + " (" + this.getProtocolVersion() + ")") +
                "====================";
    }
}
