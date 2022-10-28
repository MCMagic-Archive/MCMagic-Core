package us.mcmagic.mcmagiccore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.mcmagic.mcmagiccore.MCMagicCore;
import us.mcmagic.mcmagiccore.player.PlayerUtil;

/**
 * Created by Marc on 6/20/15
 */
public class Commandtoken implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, Command command, String label, String[] args) {
        boolean isPlayer = sender instanceof Player;
        if (args.length == 0) {
            if (isPlayer) {
                Bukkit.getScheduler().runTaskAsynchronously(MCMagicCore.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        sender.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Your Tokens: " + ChatColor.GREEN +
                                "✪ " + MCMagicCore.economy.getTokens(((Player) sender).getUniqueId()));
                    }
                });
            } else {
                helpMenu(sender);
            }
            return true;
        }
        if (args.length == 1) {
            final String user = args[0];
            Bukkit.getScheduler().runTaskAsynchronously(MCMagicCore.getInstance(), new Runnable() {
                @Override
                public void run() {
                    sender.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Tokens for " + user + ": " +
                            ChatColor.GREEN + "✪ " + MCMagicCore.economy.getTokens(sender, user));
                }
            });
            return true;
        }
        if (args.length == 2) {
            if (!isPlayer) {
                helpMenu(sender);
            } else {
                String action = args[0];
                Player tp = (Player) sender;
                if (!isInt(args[1])) {
                    helpMenu(sender);
                    return true;
                }
                String source = tp.getName();
                int amount = Integer.parseInt(args[1]);
                switch (action.toLowerCase()) {
                    case "set":
                        MCMagicCore.economy.setTokens(tp.getUniqueId(), amount, source, true);
                        break;
                    case "add":
                        MCMagicCore.economy.addTokens(tp.getUniqueId(), amount, source);
                        break;
                    case "minus":
                        MCMagicCore.economy.addTokens(tp.getUniqueId(), -amount, source);
                        break;
                }
            }
            return true;
        }
        if (args.length == 3) {
            String action = args[0];
            Player tp = PlayerUtil.findPlayer(args[2]);
            if (tp == null) {
                sender.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " is not online!");
                return true;
            }
            if (!isInt(args[1])) {
                helpMenu(sender);
                return true;
            }
            String source = sender instanceof BlockCommandSender ? "Command Block" : (sender instanceof Player ?
                    sender.getName() : "Console");
            if (sender instanceof BlockCommandSender) {
                BlockCommandSender s = (BlockCommandSender) sender;
                Location loc = s.getBlock().getLocation();
                source += " x: " + loc.getBlockX() + " y: " + loc.getBlockY() + " z: " + loc.getBlockZ();
            }
            int amount = Integer.parseInt(args[1]);
            switch (action.toLowerCase()) {
                case "set":
                    MCMagicCore.economy.setTokens(tp.getUniqueId(), amount, source, true);
                    break;
                case "add":
                    MCMagicCore.economy.addTokens(tp.getUniqueId(), amount, source);
                    break;
                case "minus":
                    MCMagicCore.economy.addTokens(tp.getUniqueId(), -amount, source);
                    break;
            }
            return true;
        }
        helpMenu(sender);
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

    private void helpMenu(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW
                + "/tokens <player> - Gets the amount of tokens a player has.");
        sender.sendMessage(ChatColor.YELLOW
                + "/tokens <set,add,minus> <amount> [player] - Changes the amount of tokens a player has.");
    }
}
