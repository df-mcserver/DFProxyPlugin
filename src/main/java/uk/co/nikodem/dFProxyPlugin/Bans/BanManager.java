package uk.co.nikodem.dFProxyPlugin.Bans;

import com.velocitypowered.api.proxy.Player;
import org.jetbrains.annotations.Nullable;
import uk.co.nikodem.dFProxyPlugin.Player.Data.PlayerData;

import java.util.UUID;

import static uk.co.nikodem.dFProxyPlugin.Player.Data.PlayerDataHandler.*;

public class BanManager {

    public void setBanInformation(Player plr, @Nullable BanInformation info) {
        setBanInformation(plr.getUniqueId(), info);
    }

    public void setBanInformation(UUID uuid, @Nullable BanInformation info) {
        PlayerData data = retrievePlayerData(uuid);

        data.banInformation = info;
        writePlayerDataToPlayerFile(uuid, data);
    }

    public void removeBanInformation(Player plr) {
        removeBanInformation(plr.getUniqueId());
    }

    public void removeBanInformation(UUID uuid) {
        setBanInformation(uuid, null);
    }
}
