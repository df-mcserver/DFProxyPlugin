package uk.co.nikodem.dFProxyPlugin.Player.Versions;

import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.util.ModInfo;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.jetbrains.annotations.Nullable;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;
import uk.co.nikodem.dFProxyPlugin.Player.ParsedPlayerInformation;

import java.util.*;

public class JavaPlayerInformation implements ParsedPlayerInformation {
    private UUID uuid;
    private int protocolVersion;
    private String minecraftVersion;
    private String username;
    private List<String> mods;
    private String brandName;

    public JavaPlayerInformation(UUID uuid) {
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

        this.mods = new ArrayList<>();

        Player plr = getPlayer();
        this.username = plr == null ? "Unknown" : getPlayer().getUsername();
        this.brandName = plr == null ? "Unknown" : plr.getClientBrand();
        Optional<ModInfo> modInfoMaybe = plr.getModInfo();
        modInfoMaybe.ifPresent(modInfo -> {
            modInfo.getMods().stream().map(mod -> this.mods.add(mod.getId()));
        });
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
    public List<String> getMods() {
        return this.mods;
    }

    @Override
    public Boolean isModded() {
        // obviously not foolproof, but good enough
        return !Objects.equals(this.brandName, "vanilla");
    }
}
