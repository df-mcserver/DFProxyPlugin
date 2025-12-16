package uk.co.nikodem.dFProxyPlugin.Discord.Utils;

import com.velocitypowered.api.proxy.Player;

public class MCAvatarURLHelper {
    public static String getAvatarURL(Player plr) {
        return "https://mc-heads.net/head/"+plr.getGameProfile().getUndashedId()+"/100";
    }
}
