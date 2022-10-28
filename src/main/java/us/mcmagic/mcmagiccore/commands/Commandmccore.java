package us.mcmagic.mcmagiccore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import us.mcmagic.mcmagiccore.MCMagicCore;
import us.mcmagic.mcmagiccore.listeners.PlayerJoinAndLeave;
import us.mcmagic.mcmagiccore.title.TitleObject;
import us.mcmagic.mcmagiccore.title.TitleObject.TitleType;

public class Commandmccore implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                sender.sendMessage(ChatColor.BLUE + "Reloading MCMagicCore...");
                MCMagicCore.getInstance().refreshConfig();
                PlayerJoinAndLeave.initialize();
                MCMagicCore.resourceManager.reload();
                if (sender instanceof Player) {
                    TitleObject reloaded = new TitleObject(ChatColor.GREEN + "Config Reloaded!", TitleType.TITLE)
                            .setFadeIn(-1).setStay(-1).setFadeOut(-1);
                    reloaded.send((Player) sender);
                }
                MCMagicCore.achievementManager.reload();
                sender.sendMessage(ChatColor.BLUE + "MCMagicCore Reloaded!");
                return true;
            }
        }
        PluginDescriptionFile desc = MCMagicCore.getInstance().getDescription();
        sender.sendMessage(ChatColor.RED + "MCMagicCore " + ChatColor.GOLD + "v" + desc.getVersion() + ChatColor.AQUA +
                " by: " + desc.getAuthors().get(0));
        return true;
    }
}