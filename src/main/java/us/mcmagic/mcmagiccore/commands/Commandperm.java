package us.mcmagic.mcmagiccore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.mcmagic.mcmagiccore.MCMagicCore;
import us.mcmagic.mcmagiccore.dashboard.packets.dashboard.PacketRankChange;
import us.mcmagic.mcmagiccore.permissions.Rank;
import us.mcmagic.mcmagiccore.player.PlayerUtil;
import us.mcmagic.mcmagiccore.player.User;
import us.mcmagic.mcmagiccore.uuidconverter.UUIDConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Marc on 11/18/14
 */
public class Commandperm implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            String arg1 = args[0];
            if (arg1.equalsIgnoreCase("refresh")) {
                sender.sendMessage(ChatColor.YELLOW + "Permissions refreshing...");
                MCMagicCore.permManager.refreshPermissions(sender);
                return true;
            }
            if (arg1.equalsIgnoreCase("list")) {
                sender.sendMessage(ChatColor.YELLOW + "Valid Usage: " + ChatColor.GREEN + "/perm list groups");
                return true;
            }
            helpMenu(sender, arg1.equalsIgnoreCase("player") ? "player" : arg1.equalsIgnoreCase("group") ? "group" : arg1.equalsIgnoreCase("list") ? "list" : "main");
            return true;
        }
        if (args.length == 2) {
            String arg1 = args[0];
            String arg2 = args[1];
            if (arg1.equalsIgnoreCase("list")) {
                if (arg2.equalsIgnoreCase("groups")) {
                    listGroups(sender);
                    return true;
                }
                helpMenu(sender, "list");
                return true;
            }
        }
        if (args.length == 3) {
            String arg1 = args[0];
            String arg2 = args[1];
            String arg3 = args[2];
            if (arg1.equalsIgnoreCase("player")) {
                if (arg3.equalsIgnoreCase("setgroup")) {
                    helpMenu(sender, "player");
                    return true;
                }
                if (!MCMagicCore.permSqlUtil.playerExists(arg2)) {
                    sender.sendMessage(ChatColor.RED + "Player '" + arg2 + "' not found!");
                    return true;
                }
                if (arg3.equalsIgnoreCase("getgroup")) {
                    Rank rank = MCMagicCore.permSqlUtil.getRank(UUIDConverter.convertFromName(arg2));
                    sender.sendMessage(ChatColor.BLUE + arg2 + ChatColor.YELLOW + " is in the " + rank.getNameWithBrackets() + ChatColor.YELLOW + " group.");
                    return true;
                }
                helpMenu(sender, "player");
                return true;
            }
            if (arg1.equalsIgnoreCase("group")) {
                Rank rank = Rank.fromString(arg2);
                if (arg3.equalsIgnoreCase("members")) {
                    listMembers(sender, rank);
                    return true;
                }
                if (arg3.equalsIgnoreCase("perms")) {
                    sender.sendMessage(ChatColor.GREEN + "Permissions of the " + rank.getNameWithBrackets() + ChatColor.GREEN + "Rank:");
                    HashMap<String, Boolean> perms = rank.getPermissions();
                    for (Map.Entry<String, Boolean> perm : perms.entrySet()) {
                        if (perm.getValue()) {
                            sender.sendMessage(ChatColor.DARK_GREEN + "- " + ChatColor.YELLOW + perm.getKey() + " " + ChatColor.GREEN + "True");
                        } else {
                            sender.sendMessage(ChatColor.DARK_GREEN + "- " + ChatColor.YELLOW + perm.getKey() + " " + ChatColor.RED + "False");
                        }
                    }
                    return true;
                }
            }
            helpMenu(sender, arg1.equalsIgnoreCase("group") ? "group" : arg1.equalsIgnoreCase("list") ? "list" : "main");
            return true;
        }
        if (args.length == 4) {
            String arg1 = args[0];
            String arg2 = args[1];
            String arg3 = args[2];
            String arg4 = args[3];
            if (arg1.equalsIgnoreCase("player")) {
                if (!MCMagicCore.permSqlUtil.playerExists(arg2)) {
                    sender.sendMessage(ChatColor.RED + "Player not found!");
                    return true;
                }
                switch (arg3) {
                    case "setgroup":
                        Rank rank = Rank.fromString(arg4);
                        final Rank currentRank = MCMagicCore.permSqlUtil.getRank(arg2);
                        Player tp = PlayerUtil.findPlayer(arg2);
                        UUID uuid = null;
                        if (tp != null) {
                            uuid = tp.getUniqueId();
                            User user = MCMagicCore.getUser(uuid);
                            user.setRank(rank);
                        } else {
                            uuid = UUIDConverter.convertFromName(arg2);
                        }
                        MCMagicCore.permSqlUtil.setRank(uuid, rank);
                        String source = sender instanceof Player ? sender.getName() : "Console on " +
                                MCMagicCore.getMCMagicConfig().instanceName;
                        PacketRankChange packet = new PacketRankChange(uuid, rank, source);
                        MCMagicCore.dashboardConnection.send(packet);
                        sender.sendMessage(ChatColor.YELLOW + arg2 + "'s rank has been changed to " + rank.getNameWithBrackets());
                        return true;
                    case "get":
                        final Rank currentRank2 = MCMagicCore.permSqlUtil.getRank(UUIDConverter.convertFromName(arg2));
                        HashMap<String, Boolean> permissions2 = currentRank2.getPermissions();
                        if (!permissions2.containsKey(arg4)) {
                            sender.sendMessage(currentRank2.getNameWithBrackets() + ChatColor.YELLOW + " does not set " + ChatColor.RED + arg4);
                            return true;
                        }
                        if (permissions2.get(arg4)) {
                            sender.sendMessage(currentRank2.getNameWithBrackets() + ChatColor.YELLOW + " sets " + ChatColor.YELLOW + arg4 + ChatColor.YELLOW + " to " + ChatColor.GREEN + "true");
                        } else {
                            sender.sendMessage(currentRank2.getNameWithBrackets() + ChatColor.YELLOW + " sets " + ChatColor.YELLOW + arg4 + ChatColor.YELLOW + " to " + ChatColor.RED + "false");
                        }
                        return true;
                    default:
                        helpMenu(sender, "player");
                        return true;
                }
            }
            if (arg1.equalsIgnoreCase("group")) {
                Rank rank = Rank.fromString(arg2);
                HashMap<String, Boolean> permissions = rank.getPermissions();
                switch (arg3) {
                    case "get":
                        if (!permissions.containsKey(arg4)) {
                            sender.sendMessage(rank.getNameWithBrackets() + ChatColor.YELLOW + " does not set " + ChatColor.RED + arg4);
                            return true;
                        }
                        if (permissions.get(arg4)) {
                            sender.sendMessage(rank.getNameWithBrackets() + ChatColor.YELLOW + " sets " + ChatColor.YELLOW + arg4 + ChatColor.YELLOW + " to " + ChatColor.GREEN + ChatColor.BOLD + "true");
                        } else {
                            sender.sendMessage(rank.getNameWithBrackets() + ChatColor.YELLOW + " sets " + ChatColor.YELLOW + arg4 + ChatColor.YELLOW + " to " + ChatColor.RED + ChatColor.BOLD + "false");
                        }
                        return true;
                    case "set":
                        MCMagicCore.permSqlUtil.setPermission(arg4, rank, true);
                        for (User user : MCMagicCore.getUsers()) {
                            if (user.getRank().getSqlName().equals(rank.getSqlName())) {
                                MCMagicCore.permManager.attachments.get(user.getUniqueId()).setPermission(arg4, true);
                            }
                        }
                        sender.sendMessage(rank.getNameWithBrackets() + ChatColor.YELLOW + " now sets " + ChatColor.AQUA + arg4 + ChatColor.YELLOW + " to " + ChatColor.GREEN + "" + ChatColor.BOLD + "true");
                        return true;
                    case "unset":
                        MCMagicCore.permSqlUtil.unsetPermission(arg4, rank);
                        for (User user : MCMagicCore.getUsers()) {
                            if (user.getRank().getSqlName().equals(rank.getSqlName())) {
                                MCMagicCore.permManager.attachments.get(user.getUniqueId()).unsetPermission(arg4);
                            }
                        }
                        sender.sendMessage(rank.getNameWithBrackets() + ChatColor.YELLOW + " does not set " + ChatColor.AQUA + arg4 + ChatColor.YELLOW + " anymore");
                        return true;
                    default:
                        helpMenu(sender, "group");
                        return true;
                }
            }
        }
        if (args.length == 5) {
            String arg1 = args[0];
            String arg2 = args[1];
            String arg3 = args[2];
            String arg4 = args[3];
            String arg5 = args[4];
            if (arg1.equalsIgnoreCase("group")) {
                Rank rank = Rank.fromString(arg2);
                HashMap<String, Boolean> permissions = rank.getPermissions();
                if (arg3.equalsIgnoreCase("set")) {
                    boolean value = arg5.equalsIgnoreCase("true");
                    MCMagicCore.permSqlUtil.setPermission(arg4, rank, value);
                    for (User user : MCMagicCore.getUsers()) {
                        if (user.getRank().getSqlName().equals(rank.getSqlName())) {
                            MCMagicCore.permManager.attachments.get(user.getUniqueId()).setPermission(arg4, value);
                        }
                    }
                    if (value) {
                        sender.sendMessage(rank.getNameWithBrackets() + ChatColor.YELLOW + " now sets " + ChatColor.AQUA + arg4 + ChatColor.YELLOW + " to " + ChatColor.GREEN + "" + ChatColor.BOLD + "true");
                        return true;
                    }
                    sender.sendMessage(rank.getNameWithBrackets() + ChatColor.YELLOW + " now sets " + ChatColor.AQUA + arg4 + ChatColor.YELLOW + " to " + ChatColor.RED + "" + ChatColor.BOLD + "false");
                    return true;
                }
                helpMenu(sender, "group");
                return true;
            }
        }
        helpMenu(sender, "main");
        return true;
    }

    public static void helpMenu(CommandSender sender, String menu) {
        String playerKey = ChatColor.LIGHT_PURPLE + "<player>";
        String groupKey = ChatColor.LIGHT_PURPLE + "<group>";
        sender.sendMessage(ChatColor.YELLOW + "----------------------------------------------------");
        switch (menu) {
            case "main":
                sender.sendMessage(ChatColor.YELLOW + "/perm player " + ChatColor.LIGHT_PURPLE + "<player> " + ChatColor.GREEN + "- Player commands");
                sender.sendMessage(ChatColor.YELLOW + "/perm group " + ChatColor.LIGHT_PURPLE + "<group> " + ChatColor.GREEN + "- Group commands");
                sender.sendMessage(ChatColor.YELLOW + "/perm list " + ChatColor.LIGHT_PURPLE + "<what> " + ChatColor.GREEN + "- List groups");
                sender.sendMessage(ChatColor.YELLOW + "/perm refresh " + ChatColor.GREEN + "- Refresh permissions for all groups");
                return;
            case "player":
                sender.sendMessage(ChatColor.YELLOW + "/perm player " + ChatColor.LIGHT_PURPLE + "<player> " + ChatColor.YELLOW + "get " + ChatColor.LIGHT_PURPLE + "<permission> " + ChatColor.YELLOW + "- Get the value of a permission for a player");
                sender.sendMessage(ChatColor.YELLOW + "/perm player " + ChatColor.LIGHT_PURPLE + "<player> " + ChatColor.YELLOW + "getgroup " + ChatColor.YELLOW + "- Get the player's rank.");
                sender.sendMessage(ChatColor.YELLOW + "/perm player " + ChatColor.LIGHT_PURPLE + "<player> " + ChatColor.YELLOW + "setgroup " + ChatColor.LIGHT_PURPLE + "<group> " + ChatColor.YELLOW + "- Set a player's rank");
                return;
            case "group":
                sender.sendMessage(ChatColor.YELLOW + "/perm group " + ChatColor.LIGHT_PURPLE + "<group> " + ChatColor.YELLOW + "get " + ChatColor.LIGHT_PURPLE + "<permission> " + ChatColor.YELLOW + "- Get the value of a permission for a rank");
                sender.sendMessage(ChatColor.YELLOW + "/perm group " + ChatColor.LIGHT_PURPLE + "<group> " + ChatColor.YELLOW + "set " + ChatColor.LIGHT_PURPLE + "<permission> " + ChatColor.AQUA + "[true/false] " + ChatColor.YELLOW + "- Set the value of a permission for a rank");
                sender.sendMessage(ChatColor.YELLOW + "/perm group " + ChatColor.LIGHT_PURPLE + "<group> " + ChatColor.YELLOW + "unset " + ChatColor.LIGHT_PURPLE + "<permission> " + ChatColor.YELLOW + "- Remove the value of a permission for a rank");
                sender.sendMessage(ChatColor.YELLOW + "/perm group " + ChatColor.LIGHT_PURPLE + "<group> " + ChatColor.YELLOW + "members " + ChatColor.YELLOW + "- List the members of a rank");
                return;
            case "list":
                sender.sendMessage(ChatColor.YELLOW + "/perm list " + ChatColor.LIGHT_PURPLE + "<what> " + ChatColor.GREEN + "- List groups");
                return;
            default:
                sender.sendMessage(ChatColor.YELLOW + "/perm player " + ChatColor.LIGHT_PURPLE + "<player> " + ChatColor.GREEN + "- Player commands");
                sender.sendMessage(ChatColor.YELLOW + "/perm group " + ChatColor.LIGHT_PURPLE + "<group> " + ChatColor.GREEN + "- Group commands");
                sender.sendMessage(ChatColor.YELLOW + "/perm list " + ChatColor.LIGHT_PURPLE + "<what> " + ChatColor.GREEN + "- List groups");
                sender.sendMessage(ChatColor.YELLOW + "/perm refresh " + ChatColor.GREEN + "- Refresh permissions for all groups");
        }
    }

    public static void listGroups(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "----------------------------------------------------");
        sender.sendMessage(ChatColor.GREEN + "Groups:");
        for (Rank rank : Rank.values()) {
            sender.sendMessage(ChatColor.YELLOW + "- " + rank.getTagColor() + rank.getName().toLowerCase().replaceAll(" ", ""));
        }
    }

    public static void listMembers(CommandSender sender, Rank rank) {
        List<String> members = MCMagicCore.permSqlUtil.getMembers(rank);
        StringBuilder sb = new StringBuilder();
        for (String name : members) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(name);
        }
        sender.sendMessage(ChatColor.YELLOW + "Members of the " + rank.getNameWithBrackets() + ChatColor.YELLOW + " rank: " + sb.toString());
    }
}