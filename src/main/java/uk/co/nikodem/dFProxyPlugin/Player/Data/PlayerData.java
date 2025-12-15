package uk.co.nikodem.dFProxyPlugin.Player.Data;

import uk.co.nikodem.dFProxyPlugin.Bans.BannedPlayer;
import uk.co.nikodem.dFProxyPlugin.Player.Platform.ParsedPlatformInformation;

public class PlayerData {
    public int joinCount = 0;
    public boolean lastWasOnBedrock = false;
    public boolean hasPlayedOnBedrock = false;
    public int bedrockJoinCount = 0;
    public long lastJoinDate = 0;
    public BannedPlayer banInformation;
    public ParsedPlatformInformation lastPlatform;
}
