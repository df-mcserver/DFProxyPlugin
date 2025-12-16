package uk.co.nikodem.dFProxyPlugin;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.viaversion.viaversion.velocity.platform.VelocityViaAPI;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.event.EventRegistrar;
import org.geysermc.geyser.api.event.bedrock.ClientEmoteEvent;
import org.slf4j.Logger;
import uk.co.nikodem.dFProxyPlugin.Bans.BanManager;
import uk.co.nikodem.dFProxyPlugin.Commands.DFCommand;
import uk.co.nikodem.dFProxyPlugin.Commands.DFCommands.Banning.BanCommand;
import uk.co.nikodem.dFProxyPlugin.Commands.DFCommands.Banning.UnbanCommand;
import uk.co.nikodem.dFProxyPlugin.Commands.DFCommands.Data.PlatformCommand;
import uk.co.nikodem.dFProxyPlugin.Commands.DFCommands.Data.RequestDataCommand;
import uk.co.nikodem.dFProxyPlugin.Commands.DFCommands.LobbyCommand;
import uk.co.nikodem.dFProxyPlugin.Commands.DFCommands.Multiplayer.PlayerlistCommand;
import uk.co.nikodem.dFProxyPlugin.Config.Config;
import uk.co.nikodem.dFProxyPlugin.Config.ConfigManager;
import uk.co.nikodem.dFProxyPlugin.Discord.DiscordBotHoster;
import uk.co.nikodem.dFProxyPlugin.Messaging.PluginMessageListener;
import uk.co.nikodem.dFProxyPlugin.Player.Bedrock.EmoteMenu;
import uk.co.nikodem.dFProxyPlugin.Player.Data.PlayerData;
import uk.co.nikodem.dFProxyPlugin.Player.Data.PlayerDataHandler;
import uk.co.nikodem.dFProxyPlugin.Player.Data.UUIDConversionHandler;
import uk.co.nikodem.dFProxyPlugin.Player.Platform.ParsedPlatformInformation;
import uk.co.nikodem.dFProxyPlugin.ResourcePack.PackHoster;

import java.nio.file.Path;
import java.util.Date;

@Plugin(id = "dfproxyplugin",
        name = "DFProxyPlugin",
        version = "1.0-SNAPSHOT",
        authors = {"deadfry42"},
        dependencies = {
                @Dependency(id = "geyser"),
                @Dependency(id = "viaversion")
        }
)
public class DFProxyPlugin implements EventRegistrar {
    public static ProxyServer server;
    public static CommandManager commandManager;

    public static VelocityViaAPI viaAPI;
    public static GeyserApi geyser;

    public static Path dataDirectory;
    public static ConfigManager manager;
    public static Config config;

    public static PackHoster hoster;
    public static DiscordBotHoster discord;
    public static BanManager banManager;

    public static UUIDConversionHandler uuidConversionHandler;

    public static Logger logger;
    public static String name;

    @Inject
    public DFProxyPlugin(ProxyServer server, Logger logger, @DataDirectory Path dataFolder) {
        DFProxyPlugin.name = this.getClass().getAnnotation(Plugin.class).name();
        DFProxyPlugin.server = server;
        DFProxyPlugin.logger = logger;
        DFProxyPlugin.commandManager = server.getCommandManager();
        DFProxyPlugin.dataDirectory = dataFolder;
        DFProxyPlugin.viaAPI = new VelocityViaAPI();
        DFProxyPlugin.manager = new ConfigManager();
        DFProxyPlugin.uuidConversionHandler = new UUIDConversionHandler();

        if (!manager.getExists()) manager.create();
        if (!manager.getIsValidConfiguration()) {
            logger.error("Invalid configuration! Cannot initialise!");
            return;
        }
        config = manager.update();

        if (config.resource_pack_hosting.isEnabled()) DFProxyPlugin.hoster = new PackHoster();
        if (config.discord_bot.isEnabled()) DFProxyPlugin.discord = new DiscordBotHoster();
        DFProxyPlugin.banManager = new BanManager();

        PluginMessageListener.initialiseMessageHandlers();
        DFProxyPlugin.logger.info("Loaded plugin {}!", name);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // register commands
        DFCommand.registerDFCommand(this, new BanCommand());
        DFCommand.registerDFCommand(this, new UnbanCommand());
        DFCommand.registerDFCommand(this, new PlatformCommand());
        DFCommand.registerDFCommand(this, new LobbyCommand());
        DFCommand.registerDFCommand(this, new RequestDataCommand());
        DFCommand.registerDFCommand(this, new PlayerlistCommand());

        server.getChannelRegistrar().register(PluginMessageListener.IDENTIFIER);
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        if (config.resource_pack_hosting.isEnabled()) DFProxyPlugin.hoster.gracefullyShutdown();
        DFProxyPlugin.logger.info("Deloaded plugin {}!", name);
    }

    @Subscribe
    public void onPlayerDisconnect(DisconnectEvent event) {
        if (geyser == null) geyser = GeyserApi.api();
    }

    @Subscribe
    public void onPlayerJoin(ServerConnectedEvent event) {
        // i put the initialisation of geyser here to make sure geyser is fully loaded first lol
        if (geyser == null) geyser = GeyserApi.api();
        geyser.eventBus().register(this, this);

        if (event.getPreviousServer().isEmpty()) {
            Player plr = event.getPlayer();
            LoginAttempt login = new LoginAttempt(plr, DisconnectEvent.LoginStatus.SUCCESSFUL_LOGIN);
            if (config.login.isLoginMessageEnabled()) System.out.println(login);

            DFProxyPlugin.uuidConversionHandler.addConversion(plr);

            ParsedPlatformInformation info = ParsedPlatformInformation.fromUUID(plr.getUniqueId());
            PlayerData data = PlayerDataHandler.onJoin(plr, info);

            if (data.banInformation != null) {
                if (data.banInformation.getEnd() < new Date().getTime() && !data.banInformation.isPermanentlyBanned()) {
                    // ban is expired
                    data.banInformation = null;
                    PlayerDataHandler.writePlayerDataToPlayerFile(plr, data);
                } else {
                    plr.disconnect(data.banInformation.getBanMessage());
                }
            }
        }
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        PluginMessageListener.onPluginMessage(event);
    }

    @org.geysermc.event.subscribe.Subscribe
    public void onGeyserPlayerUseEmote(ClientEmoteEvent event) {
        EmoteMenu.handleEmote(event);
    }
}
