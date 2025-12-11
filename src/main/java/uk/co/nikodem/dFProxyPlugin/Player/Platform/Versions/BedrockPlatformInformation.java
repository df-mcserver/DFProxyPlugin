package uk.co.nikodem.dFProxyPlugin.Player.Platform.Versions;

import com.velocitypowered.api.proxy.Player;
import org.geysermc.geyser.api.connection.GeyserConnection;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;
import uk.co.nikodem.dFProxyPlugin.Player.Platform.ParsedPlatformInformation;

import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.UUID;

public class BedrockPlatformInformation implements ParsedPlatformInformation {
    private final UUID uuid;
    private final String minecraftVersion;
    private final String devicePlatformName;
    private final String serverUsername;
    private final String realUsername;
    private final Integer protocolVersion;

    public BedrockPlatformInformation(UUID uuid) {
        this.uuid = uuid;

        this.minecraftVersion = getGeyserConnection().version();
        this.devicePlatformName = getGeyserConnection().platform().toString();
        this.serverUsername = getPlayer() == null ? "Unknown" : getPlayer().getUsername();
        this.realUsername = getGeyserConnection().bedrockUsername();
        this.protocolVersion = getGeyserConnection().protocolVersion();
    }

    public BedrockPlatformInformation(String uuid, int protocolVersion, String minecraftVersion, String devicePlatformName, String serverUsername, String realUsername) {
        this.uuid = UUID.fromString(uuid);
        this.minecraftVersion = minecraftVersion;
        this.devicePlatformName = devicePlatformName;
        this.serverUsername = serverUsername;
        this.realUsername = realUsername;
        this.protocolVersion = protocolVersion;
    }

    @Override
    public String getServerUsername() {
        return this.serverUsername;
    }

    @Override
    public String getRealUsername() {
        return this.realUsername;
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
        return getMinecraftPlatformName() + " ("+getDevicePlatformName()+")";
    }

    @Override
    public String getMinecraftPlatformName() {
        return "Bedrock";
    }

    @Override
    public String getDevicePlatformName() {
        return this.devicePlatformName;
    }

    @Override
    public int getProtocolVersion() {
        return protocolVersion;
    }

    @Override
    public String getMinecraftVersion() {
        return minecraftVersion;
    }

    @Override
    public GeyserConnection getGeyserConnection() {
        return DFProxyPlugin.geyser.connectionByUuid(uuid);
    }

    @Override
    public Boolean isBedrock() {
        return true;
    }

    @Override
    public String getClientBrandName() {
        return "vanilla";
    }

    @Override
    public boolean isIncompatible() {
        return false; // no way to check bedrock for incompatibilities
    }

    @Override
    public Boolean isModded() {
        // Bedrock clients can be modded
        // but that would be literal hacks so whatever
        return false;
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
