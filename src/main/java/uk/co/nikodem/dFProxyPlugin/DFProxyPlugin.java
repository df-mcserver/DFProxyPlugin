package uk.co.nikodem.dFProxyPlugin;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.event.EventRegistrar;
import org.geysermc.geyser.api.event.bedrock.ClientEmoteEvent;
import org.slf4j.Logger;
import uk.co.nikodem.dFProxyPlugin.Commands.DFCommand;
import uk.co.nikodem.dFProxyPlugin.Commands.DFCommands.Banning.BanCommand;
import uk.co.nikodem.dFProxyPlugin.Commands.DFCommands.LobbyCommand;
import uk.co.nikodem.dFProxyPlugin.Commands.DFCommands.PlatformCommand;
import uk.co.nikodem.dFProxyPlugin.Commands.DFCommands.Banning.UnbanCommand;
import uk.co.nikodem.dFProxyPlugin.Messaging.PluginMessageListener;
import uk.co.nikodem.dFProxyPlugin.Player.Bedrock.EmoteMenu;
import uk.co.nikodem.dFProxyPlugin.Player.Data.PlayerData;
import uk.co.nikodem.dFProxyPlugin.Player.Data.PlayerDataHandler;
import uk.co.nikodem.dFProxyPlugin.Player.JoinMessage;
import uk.co.nikodem.dFProxyPlugin.Player.Platform.ParsedPlatformInformation;
import uk.co.nikodem.dFProxyPlugin.Player.PlayerCheckSuccess;

import java.nio.file.Path;
import java.util.Date;
import java.util.List;

@Plugin(id = "dfproxyplugin",
        name = "DFProxyPlugin",
        version = "1.0-SNAPSHOT",
        authors = {"deadfry42"},
        dependencies = {
                @Dependency(id = "geyser"),
                @Dependency(id = "floodgate")
        }
)
public class DFProxyPlugin implements EventRegistrar {
    public static ProxyServer server;
    public static CommandManager commandManager;
    public static GeyserApi geyser;
    public static Path dataDirectory;

    public static Logger logger;
    public static String name;

    @Inject
    public DFProxyPlugin(ProxyServer server, Logger logger, @DataDirectory Path dataFolder) {
        DFProxyPlugin.name = this.getClass().getAnnotation(Plugin.class).name();
        DFProxyPlugin.server = server;
        DFProxyPlugin.logger = logger;
        DFProxyPlugin.commandManager = server.getCommandManager();
        DFProxyPlugin.dataDirectory = dataFolder;

        this.onLoad();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.onEnable(event);
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        this.onDisable();
    }

    @Subscribe
    public void onPlayerDisconnect(DisconnectEvent event) {
        if (geyser == null) geyser = GeyserApi.api();
        Player plr = event.getPlayer();
        ParsedPlatformInformation.removePlayerFromCache(plr.getUniqueId());
        DisconnectEvent.LoginStatus status = event.getLoginStatus();

        if (status != DisconnectEvent.LoginStatus.PRE_SERVER_JOIN) return;

        LoginAttempt login = new LoginAttempt(plr, status);
        System.out.println(login.stringify());
    }

    @Subscribe
    public void onPlayerLogin(PreLoginEvent event) {
        assert event.getUniqueId() != null;
        String uuid = event.getUniqueId().toString();
    }

    @Subscribe
    public void onPlayerJoin(ServerConnectedEvent event) {
        // i put the initialisation of geyser here to make sure geyser is fully loaded first lol
        if (geyser == null) geyser = GeyserApi.api();
        geyser.eventBus().register(this, this);

        if (event.getPreviousServer().isEmpty()) {
            Player plr = event.getPlayer();
            LoginAttempt login = new LoginAttempt(plr, DisconnectEvent.LoginStatus.SUCCESSFUL_LOGIN);
            System.out.println(login.stringify());

            System.out.println(plr.getClientBrand());
            plr.getModInfo().ifPresent(modInfo -> {
                System.out.println(modInfo.getType());
                System.out.println(modInfo);
                modInfo.getMods().stream().map(mod -> {
                    System.out.println(mod.getId());
                    return null;
                });
            });

            ParsedPlatformInformation info = ParsedPlatformInformation.fromUUID(plr.getUniqueId());

            // while this isn't a one stop solution to stop hacking
            // it's good enough
            List<String> allowedClients = List.of("vanilla", "fabric", "neoforge", "quilt");
            PlayerCheckSuccess checkSuccess = PlayerCheckSuccess.Success;

            // vague error message so people ask me
            // and i can tell them to gtfo off forge
            if (!allowedClients.contains(info.getClientBrandName())) {
                checkSuccess = PlayerCheckSuccess.IncompatibleClient;
            }

            PlayerData data = PlayerDataHandler.onJoin(plr, info, checkSuccess);

            if (data.banInformation != null) {
                if (data.banInformation.getEnd() < new Date().getTime() && data.banInformation.getEnd() > 0) {
                    // ban is expired
                    data.banInformation = null;
                    PlayerDataHandler.writePlayerDataToPlayerFile(plr, data);
                } else {
                    String reason = data.banInformation.getReason();
                    Date endTime = new Date(data.banInformation.getEnd());
                    plr.disconnect(
                            Component.text(
                                    "You have been banned from this server!\n\nReason: ", NamedTextColor.RED
                            ).append(Component.text(
                                    reason, NamedTextColor.WHITE
                            )).append(Component.text(
                                    "\n\nYou will be unbanned on "+ endTime +".", NamedTextColor.RED
                            ))
                    );
                }
            }

            if (checkSuccess == PlayerCheckSuccess.IncompatibleClient) {
                plr.disconnect(Component.text("Client \""+info.getClientBrandName()+"\" cannot connect due to Policy violations!\nPlease use a different client and try again.", NamedTextColor.RED));
            }

            JoinMessage.sendMessage(info);
        }
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        System.out.println("Plugin message");
        PluginMessageListener.onPluginMessage(event);
    }

    @org.geysermc.event.subscribe.Subscribe
    public void onGeyserPlayerUseEmote(ClientEmoteEvent event) {
        EmoteMenu.handleEmote(event);
    }

    public void onLoad() {
        System.out.println("Loaded plugin "+ name);
    }

    public void onEnable(ProxyInitializeEvent event) {
        // register commands
        DFCommand.registerDFCommand(this, new BanCommand());
        DFCommand.registerDFCommand(this, new UnbanCommand());
        DFCommand.registerDFCommand(this, new PlatformCommand());
        DFCommand.registerDFCommand(this, new LobbyCommand());

        server.getChannelRegistrar().register(PluginMessageListener.IDENTIFIER);
    }

    public void onDisable() {
        System.out.println("Deloaded plugin "+ name);
    }
}
