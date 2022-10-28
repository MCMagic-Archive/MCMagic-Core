package us.mcmagic.mcmagiccore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.mcmagic.mcmagiccore.player.PlayerUtil;
import us.mcmagic.mcmagiccore.scoreboard.ScoreboardUtility;

/**
 * Created by Marc on 5/28/15
 */
public class Commandtagtoggle implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            ScoreboardUtility.toggleTags(player);
        } else {
            Player tp = PlayerUtil.findPlayer(args[0]);
            if (tp == null) {
                player.sendMessage(ChatColor.RED + "Player not found!");
                return true;
            }
            ScoreboardUtility.toggleTags(tp);
            player.sendMessage(ChatColor.GREEN + "Tags toggled for " + tp.getName());
        }
        return true;
    }
}
