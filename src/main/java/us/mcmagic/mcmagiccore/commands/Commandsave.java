package us.mcmagic.mcmagiccore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Marc on 11/17/14
 */
public class Commandsave implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.GRAY + "Saving Worlds...");
        for (World world : Bukkit.getWorlds()) {
            world.save();
        }
        sender.sendMessage(ChatColor.GRAY + "Worlds Saved!");
        return true;
    }
}
