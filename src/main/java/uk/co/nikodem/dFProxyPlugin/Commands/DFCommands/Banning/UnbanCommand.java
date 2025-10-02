package uk.co.nikodem.dFProxyPlugin.Commands.DFCommands.Banning;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
//import uk.co.nikodem.dFProxyPlugin.BanlistManager;
import uk.co.nikodem.dFProxyPlugin.Commands.DFCommand;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;

public class UnbanCommand implements DFCommand {
    @Override
    public BrigadierCommand createBrigadierCommand(final ProxyServer server) {
        LiteralCommandNode<CommandSource> helloNode = BrigadierCommand.literalArgumentBuilder("unban")
                .requires(source -> source.hasPermission("dfproxy.ban"))
                .executes(context -> {
                    CommandSource source = context.getSource();

                    Component message = Component.text("No uuid provided!", NamedTextColor.RED);
                    source.sendMessage(message);

                    return Command.SINGLE_SUCCESS;
                })
                .then(BrigadierCommand.requiredArgumentBuilder("uuid", StringArgumentType.word())
                        .executes(context -> {

                            String uuid = context.getArgument("uuid", String.class);

//                            if (BanlistManager.playerExists(uuid)) {
//                                String username = BanlistManager.getPlayerUsername(uuid);
//                                if (!BanlistManager.playerIsBanned(uuid)) {
//                                    context.getSource().sendMessage(Component.text(username+" isn't banned!", NamedTextColor.RED));
//                                } else {
//                                    BanlistManager.unbanPlayer(uuid);
//                                    context.getSource().sendMessage(Component.text(username+" has been unbanned!", NamedTextColor.GREEN));
//                                }
//                            } else {
//                                context.getSource().sendMessage(Component.text("Player doesn't exist!", NamedTextColor.RED));
//                            }

                            return Command.SINGLE_SUCCESS;
                        }))
                .build();

        return new BrigadierCommand(helloNode);
    }

    @Override
    public CommandMeta createCommandMeta(final DFProxyPlugin plugin, final CommandManager commandManager) {
        return commandManager.metaBuilder("unban")
                .plugin(plugin)
                .build();
    }
}
