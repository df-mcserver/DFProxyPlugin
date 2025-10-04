package uk.co.nikodem.dFProxyPlugin.Player.Platform.Versions;

import com.velocitypowered.api.proxy.Player;
import org.geysermc.geyser.api.connection.GeyserConnection;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;
import uk.co.nikodem.dFProxyPlugin.Player.Platform.ParsedPlatformInformation;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.UUID;

public class BedrockPlatformInformation implements ParsedPlatformInformation {
    private UUID uuid;
    private String minecraftVersion;
    private String devicePlatformName;
    private String serverUsername;
    private String realUsername;

    public BedrockPlatformInformation(UUID uuid) {
        this.uuid = uuid;

        this.minecraftVersion = getGeyserConnection().version();
        this.devicePlatformName = getGeyserConnection().platform().toString();
        if (this.devicePlatformName.equals("Android")) {
            this.devicePlatformName = "Android/Linux";
        }

        this.serverUsername = getPlayer() == null ? "Unknown" : getPlayer().getUsername();
        this.realUsername = getGeyserConnection().bedrockUsername();
    }

    public BedrockPlatformInformation(String uuid, String minecraftVersion, String devicePlatformName, String serverUsername, String realUsername) {
        this.uuid = UUID.fromString(uuid);
        this.minecraftVersion = minecraftVersion;
        this.devicePlatformName = devicePlatformName;
        this.serverUsername = serverUsername;
        this.realUsername = realUsername;
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
    public Boolean isModded() {
        // Bedrock clients can be modded
        // but that would be literal hacks so whatever
        return false;
    }
}
