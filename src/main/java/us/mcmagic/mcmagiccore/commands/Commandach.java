package us.mcmagic.mcmagiccore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.mcmagic.mcmagiccore.MCMagicCore;
import us.mcmagic.mcmagiccore.achievements.MagicAchievement;
import us.mcmagic.mcmagiccore.player.User;

/**
 * Created by Marc on 6/26/16
 */
public class Commandach implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "/ach [ID] [Username]");
            return true;
        }
        if (!isInt(args[0])) {
            sender.sendMessage(ChatColor.RED + "/ach [ID] [Username]");
            return true;
        }
        Integer id = Integer.parseInt(args[0]);
        MagicAchievement ach = MCMagicCore.achievementManager.getAchievement(id);
        if (ach == null) {
            sender.sendMessage(ChatColor.RED + "There is no achievement with ID " + id + "!");
            return true;
        }
        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return true;
        }
        User user = MCMagicCore.getUser(player.getUniqueId());
        user.giveAchievement(id);
        return true;
    }

    private boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }
}