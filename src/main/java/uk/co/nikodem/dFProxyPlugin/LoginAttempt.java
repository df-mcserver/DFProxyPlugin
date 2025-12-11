package uk.co.nikodem.dFProxyPlugin;

import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import uk.co.nikodem.dFProxyPlugin.Player.Platform.ParsedPlatformInformation;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LoginAttempt {
    private final ParsedPlatformInformation information;
    public final Player plr;
    public final long time;
    public final String readableTime;

    public String type;

    public LoginAttempt(Player plr, DisconnectEvent.LoginStatus status) {
        this.plr = plr;

        this.information = ParsedPlatformInformation.fromPlayer(plr);

        Date date = Calendar.getInstance().getTime();
        this.time = date.getTime();
        this.readableTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);

        if (status == DisconnectEvent.LoginStatus.SUCCESSFUL_LOGIN) {
            this.type = "Success";
        } else {
            this.type = "Disconnection";
        }
    }

    @Override
    public String toString() {
        return "---------------------------------------------\n" +
                MessageFormat.format("Login attempt by {0}\n", information.getServerUsername().equals(information.getRealUsername()) ? information.getServerUsername() : information.getServerUsername()+" (aka "+information.getRealUsername()+")") +
                MessageFormat.format("UUID: {0}\n", information.getUniqueId().toString()) +
                MessageFormat.format("Client: {0}{1}\n", information.getClientBrandName(), information.isModded() ? " (likely modded)" : "") +
                MessageFormat.format("Platform: {0}\n", information.getPlatformName()) +
                MessageFormat.format("Version: {0}\n", information.getMinecraftVersion() + " (" + information.getProtocolVersion() + ")") +
                MessageFormat.format("Time: {0} {1}\n", readableTime, time) +
                MessageFormat.format("Type: {0}\n", type) +
                "---------------------------------------------";
    }

    public ParsedPlatformInformation getInformation() {
        return this.information;
    }
}
