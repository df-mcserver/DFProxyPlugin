package uk.co.nikodem.dFProxyPlugin;

import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.proxy.Player;
import org.geysermc.geyser.api.connection.GeyserConnection;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LoginAttempt {
    public String uuid;
    public String username;
    public long time;
    public String readableTime;
    public int protocolVer;
    public boolean bedrock;
    public String platform;
    public String readableVer;

    public final Player plr;

    public String type;

    public static DFProxyPlugin plugin;

    public LoginAttempt(Player plr, DisconnectEvent.LoginStatus status) {
        this.plr = plr;

        setBedrock(plr);
        setStatus(status);
        setName(plr);
        setUuid(plr);
        setVersion(plr);

        setTime();
    }

    public String stringify() {
        String string = "";
        string += "---------------------------------------------";
        string += "\nLoginAttempt by "+username+" ("+uuid+")";
        string += "\nVersion: "+readableVer+" ("+protocolVer+")";
        string += "\nTime: "+readableTime+" ("+time+")";
        string += "\nPlatform: "+platform;
        string += "\nType: "+type;
        string += "\n---------------------------------------------";
        return string;
    }

    private void setStatus(DisconnectEvent.LoginStatus status) {
        if (status == DisconnectEvent.LoginStatus.SUCCESSFUL_LOGIN) {
            this.type = "Success";
        } else {
            this.type = "Disconnection";
        }
    }

    private void setBedrock(Player plr) {
        if (plugin.geyser != null) {
            this.bedrock = plugin.geyser.isBedrockPlayer(plr.getUniqueId());
        } else {
            this.bedrock = false;
        }
    }

    private void setVersion(Player plr) {
        if (bedrock) {
            GeyserConnection connection = plugin.geyser.connectionByUuid(plr.getUniqueId());

            this.protocolVer = -1;

            if (connection == null) {
                this.readableVer = "error getting geyser connection";
                this.platform = "error getting geyser connection";
                return;
            };

            this.readableVer = connection.version();
            this.platform = connection.platform().toString();

            if (connection.isConsole()) {
                platform += " (Console)";
            }

            return;
        }

        ProtocolVersion v = plr.getProtocolVersion();

        this.protocolVer = v.getProtocol();
        this.platform = "Java";

        this.readableVer = "MINECRAFT_Unknown";

        if (this.protocolVer == 768) {
            // add seperate case for this protocol number,
            // mojang decided to group these two together for some reason ;)
            this.readableVer = "MINECRAFT_1_21_2/1_21_3";
        } else {
            for (var version : ProtocolVersion.ID_TO_PROTOCOL_CONSTANT.entrySet()) {
                int id = version.getKey();
                if (this.protocolVer == id) {
                    this.readableVer = version.getValue().name();
                    break;
                }
            }
        }

        String formatted = this.readableVer;
        formatted = formatted.replace("MINECRAFT_", "");
        formatted = formatted.replace("_", ".");

        this.readableVer = formatted;
    }

    private void setTime() {
        Date date = Calendar.getInstance().getTime();
        this.time = date.getTime();
        this.readableTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);
    }

    private void setName(Player plr) {
        this.username = plr.getUsername();
    }

    private void setUuid(Player plr) {
        this.uuid = plr.getUniqueId().toString();
    }
}
