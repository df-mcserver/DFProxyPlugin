package uk.co.nikodem.dFProxyPlugin.Player.Versions;

import com.velocitypowered.api.proxy.Player;
import org.geysermc.geyser.api.connection.GeyserConnection;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;
import uk.co.nikodem.dFProxyPlugin.Player.ParsedPlayerInformation;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BedrockPlayerInformation implements ParsedPlayerInformation {
    private UUID uuid;
    private String minecraftVersion;
    private String devicePlatformName;
    private String serverUsername;
    private String realUsername;

    public BedrockPlayerInformation(UUID uuid) {
        this.uuid = uuid;

        this.minecraftVersion = getGeyserConnection().version();
        this.devicePlatformName = getGeyserConnection().platform().toString();
        if (this.devicePlatformName.equals("Android")) {
            this.devicePlatformName = "Android/Linux";
        }

        this.serverUsername = getPlayer() == null ? "Unknown" : getPlayer().getUsername();
        this.realUsername = getGeyserConnection().bedrockUsername();
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
        return -1; // geyser doesn't expose a protocol version
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
    public List<String> getMods() {
        return List.of();
    }

    @Override
    public Boolean isModded() {
        // Bedrock clients can be modded
        // but that would be literal hacks so whatever
        return false;
    }
}
