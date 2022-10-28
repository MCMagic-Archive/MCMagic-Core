package us.mcmagic.mcmagiccore.permissions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import us.mcmagic.mcmagiccore.MCMagicCore;
import us.mcmagic.mcmagiccore.player.User;
import us.mcmagic.mcmagiccore.scoreboard.ScoreboardUtility;

import java.util.*;

public class PermManager {
    public HashMap<UUID, PermissionAttachment> attachments = new HashMap<>();
    public HashMap<String, Boolean> ownerPerms = new HashMap<>();
    public HashMap<String, Boolean> mayorPerms = new HashMap<>();
    public HashMap<String, Boolean> managerPerms = new HashMap<>();
    public HashMap<String, Boolean> devPerms = new HashMap<>();
    public HashMap<String, Boolean> coordPerms = new HashMap<>();
    public HashMap<String, Boolean> cmPerms = new HashMap<>();
    public HashMap<String, Boolean> emePerms = new HashMap<>();
    public HashMap<String, Boolean> charPerms = new HashMap<>();
    public HashMap<String, Boolean> sgPerms = new HashMap<>();
    public HashMap<String, Boolean> dvcPerms = new HashMap<>();
    public HashMap<String, Boolean> guestPerms = new HashMap<>();

    public static User createUser(UUID uuid, String name) {
        return MCMagicCore.permSqlUtil.createUser(uuid, name);
    }

    public static Rank getRank(Player player) {
        User user = MCMagicCore.getUser(player.getUniqueId());
        if (user == null) {
            player.sendMessage(ChatColor.RED +
                    "There was an error determining your permissions. Please contact a Staff Member immediately!");
            return Rank.GUEST;
        }
        return user.getRank();
    }

    public static TabCompleter getTabCompleter() {
        return new TabCompleter() {
            @Override
            public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
                List<String> groups = Arrays.asList("guest", "dvc", "shareholder", "anchornetwork", "adventureridge",
                        "magicaldreams", "craftventure", "minedisney", "mcprohosting", "specialguest", "character",
                        "earningmyears", "castmember", "coordinator", "developer", "manager", "mayor", "owner");
                List<String> groupArgs = Arrays.asList("get", "set", "unset", "members", "perms");
                List<String> groupBoolean = Arrays.asList("true", "false");
                List<String> playerArgs = Arrays.asList("get", "getgroup", "setgroup");
                List<String> listing = Collections.singletonList("groups");
                List<String> dflt = Arrays.asList("player", "group", "list", "import", "refresh");
                if (args.length == 1) {
                    List<String> list = new ArrayList<>();
                    for (String s : dflt) {
                        if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                            list.add(s);
                        }
                    }
                    return list;
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("group")) {
                        List<String> list = new ArrayList<>();
                        for (String s : groups) {
                            if (s.toLowerCase().startsWith(args[1].toLowerCase())) {
                                list.add(s);
                            }
                        }
                        return list;
                    } else if (args[0].equalsIgnoreCase("list")) {
                        List<String> list = new ArrayList<>();
                        for (String s : listing) {
                            if (s.toLowerCase().startsWith(args[1].toLowerCase())) {
                                list.add(s);
                            }
                        }
                        return list;
                    }
                    return null;
                } else if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("group")) {
                        if (!groups.contains(args[1])) {
                            sender.sendMessage(ChatColor.RED + "The group '" + ChatColor.GREEN + args[1] + ChatColor.RED
                                    + "' was not found!");
                            return new ArrayList<>();
                        }
                        List<String> list = new ArrayList<>();
                        for (String s : groupArgs) {
                            if (s.toLowerCase().startsWith(args[2].toLowerCase())) {
                                list.add(s);
                            }
                        }
                        return list;
                    }
                    if (args[0].equalsIgnoreCase("player")) {
                        List<String> list = new ArrayList<>();
                        for (String s : playerArgs) {
                            if (s.toLowerCase().startsWith(args[2].toLowerCase())) {
                                list.add(s);
                            }
                        }
                        return list;
                    }
                    return new ArrayList<>();
                } else if (args.length == 5) {
                    if (args[0].equalsIgnoreCase("group")) {
                        if (args[2].equalsIgnoreCase("set")) {
                            List<String> list = new ArrayList<>();
                            for (String s : groupBoolean) {
                                if (s.toLowerCase().startsWith(args[4].toLowerCase())) {
                                    list.add(s);
                                }
                            }
                            return list;
                        }
                    }
                    return new ArrayList<>();
                }
                return null;
            }
        };
    }

    private static void severeShutdown() {
        Bukkit.getLogger().severe("There was an error in setting up permissions, server shutting down!");
        Bukkit.shutdown();
    }

    public void onQuit(Player player, boolean sb) {
        if (sb) {
            ScoreboardUtility.logout(player.getUniqueId(), player.getName());
        }
        onQuit(player.getUniqueId());
    }

    public void onQuit(UUID uuid) {
        MCMagicCore.economy.logout(uuid);
        if (MCMagicCore.getMCMagicConfig().audioServer) {
            MCMagicCore.audioServer.logout(uuid);
        }
        attachments.remove(uuid);
        MCMagicCore.deleteUser(uuid);
    }

    public void setPermissions(Player player, Rank rank, PermissionAttachment attachment) {
        for (Map.Entry<String, Boolean> entry : attachment.getPermissions().entrySet()) {
            attachment.unsetPermission(entry.getKey());
        }
        for (Map.Entry<String, Boolean> entry : rank.getPermissions().entrySet()) {
            attachment.setPermission(entry.getKey(), entry.getValue());
        }
        attachments.put(player.getUniqueId(), attachment);
        player.recalculatePermissions();
    }

    public void assignPermissions() {
        for (User tp : MCMagicCore.getUsers()) {
            Rank rank = tp.getRank();
            PermissionAttachment attachment = attachments.get(tp.getUniqueId());
            for (Map.Entry<String, Boolean> entry : attachment.getPermissions().entrySet()) {
                attachment.unsetPermission(entry.getKey());
            }
            for (Map.Entry<String, Boolean> entry : rank.getPermissions().entrySet()) {
                attachment.setPermission(entry.getKey(), entry.getValue());
            }
        }
    }

    public void setupPermissions() {
        Bukkit.getLogger().info("Setting up Permissions...");
        try {
            HashMap<String, Boolean> ownerPerms = MCMagicCore.permSqlUtil.getPermissions(Rank.OWNER);
            HashMap<String, Boolean> mayorPerms = MCMagicCore.permSqlUtil.getPermissions(Rank.MAYOR);
            HashMap<String, Boolean> managerPerms = MCMagicCore.permSqlUtil.getPermissions(Rank.MANAGER);
            HashMap<String, Boolean> devPerms = MCMagicCore.permSqlUtil.getPermissions(Rank.DEVELOPER);
            HashMap<String, Boolean> coordPerms = MCMagicCore.permSqlUtil.getPermissions(Rank.CASTMEMBER);
            HashMap<String, Boolean> cmPerms = MCMagicCore.permSqlUtil.getPermissions(Rank.CASTMEMBER);
            HashMap<String, Boolean> emePerms = MCMagicCore.permSqlUtil.getPermissions(Rank.EARNINGMYEARS);
            HashMap<String, Boolean> charPerms = MCMagicCore.permSqlUtil.getPermissions(Rank.CHARACTER);
            HashMap<String, Boolean> sgPerms = MCMagicCore.permSqlUtil.getPermissions(Rank.SPECIALGUEST);
            HashMap<String, Boolean> dvcPerms = MCMagicCore.permSqlUtil.getPermissions(Rank.DVCMEMBER);
            HashMap<String, Boolean> guestPerms = MCMagicCore.permSqlUtil.getPermissions(Rank.GUEST);
            Rank[] ranks = new Rank[]{Rank.GUEST, Rank.DVCMEMBER, Rank.SPECIALGUEST, Rank.CHARACTER, Rank.EARNINGMYEARS,
                    Rank.CASTMEMBER, Rank.MANAGER, Rank.DEVELOPER, Rank.MAYOR, Rank.OWNER};
            for (Rank rank : ranks) {
                switch (rank) {
                    case OWNER:
                        for (Map.Entry<String, Boolean> perm : new HashSet<>(ownerPerms.entrySet())) {
                            if (this.ownerPerms.containsKey(perm.getKey())) {
                                this.ownerPerms.remove(perm.getKey());
                            }
                            this.ownerPerms.put(perm.getKey(), perm.getValue());
                        }
                        for (Map.Entry<String, Boolean> perm : new HashSet<>(mayorPerms.entrySet())) {
                            if (this.ownerPerms.containsKey(perm.getKey())) {
                                if (!this.ownerPerms.get(perm.getKey()).equals(perm.getValue())) {
                                    continue;
                                }
                            }
                            this.ownerPerms.put(perm.getKey(), perm.getValue());
                        }
                    case MAYOR:
                        for (Map.Entry<String, Boolean> perm : new HashSet<>(mayorPerms.entrySet())) {
                            if (this.mayorPerms.containsKey(perm.getKey())) {
                                this.mayorPerms.remove(perm.getKey());
                            }
                            this.mayorPerms.put(perm.getKey(), perm.getValue());
                        }
                        for (Map.Entry<String, Boolean> perm : new HashSet<>(devPerms.entrySet())) {
                            if (this.mayorPerms.containsKey(perm.getKey())) {
                                if (!this.mayorPerms.get(perm.getKey()).equals(perm.getValue())) {
                                    continue;
                                }
                            }
                            this.mayorPerms.put(perm.getKey(), perm.getValue());
                        }
                        break;
                    case DEVELOPER:
                        for (Map.Entry<String, Boolean> perm : new HashSet<>(devPerms.entrySet())) {
                            if (this.devPerms.containsKey(perm.getKey())) {
                                this.devPerms.remove(perm.getKey());
                            }
                            this.devPerms.put(perm.getKey(), perm.getValue());
                        }
                        for (Map.Entry<String, Boolean> perm : new HashSet<>(managerPerms.entrySet())) {
                            if (this.devPerms.containsKey(perm.getKey())) {
                                if (!this.devPerms.get(perm.getKey()).equals(perm.getValue())) {
                                    continue;
                                }
                            }
                            this.devPerms.put(perm.getKey(), perm.getValue());
                        }
                        break;
                    case MANAGER:
                        for (Map.Entry<String, Boolean> perm : new HashSet<>(managerPerms.entrySet())) {
                            if (this.managerPerms.containsKey(perm.getKey())) {
                                this.managerPerms.remove(perm.getKey());
                            }
                            this.managerPerms.put(perm.getKey(), perm.getValue());
                        }
                        for (Map.Entry<String, Boolean> perm : new HashSet<>(coordPerms.entrySet())) {
                            if (this.managerPerms.containsKey(perm.getKey())) {
                                if (!this.managerPerms.get(perm.getKey()).equals(perm.getValue())) {
                                    continue;
                                }
                            }
                            this.managerPerms.put(perm.getKey(), perm.getValue());
                        }
                        break;
                    case COORDINATOR:
                        for (Map.Entry<String, Boolean> perm : new HashSet<>(coordPerms.entrySet())) {
                            if (this.coordPerms.containsKey(perm.getKey())) {
                                this.coordPerms.remove(perm.getKey());
                            }
                            this.coordPerms.put(perm.getKey(), perm.getValue());
                        }
                        for (Map.Entry<String, Boolean> perm : new HashSet<>(cmPerms.entrySet())) {
                            if (this.coordPerms.containsKey(perm.getKey())) {
                                if (!this.coordPerms.get(perm.getKey()).equals(perm.getValue())) {
                                    continue;
                                }
                            }
                            this.coordPerms.put(perm.getKey(), perm.getValue());
                        }
                        break;
                    case CASTMEMBER:
                        for (Map.Entry<String, Boolean> perm : new HashSet<>(cmPerms.entrySet())) {
                            if (this.cmPerms.containsKey(perm.getKey())) {
                                this.cmPerms.remove(perm.getKey());
                            }
                            this.cmPerms.put(perm.getKey(), perm.getValue());
                        }
                        for (Map.Entry<String, Boolean> perm : new HashSet<>(emePerms.entrySet())) {
                            if (this.cmPerms.containsKey(perm.getKey())) {
                                if (!this.cmPerms.get(perm.getKey()).equals(perm.getValue())) {
                                    continue;
                                }
                            }
                            this.cmPerms.put(perm.getKey(), perm.getValue());
                        }
                        break;
                    case EARNINGMYEARS:
                        for (Map.Entry<String, Boolean> perm : new HashSet<>(emePerms.entrySet())) {
                            if (this.emePerms.containsKey(perm.getKey())) {
                                this.emePerms.remove(perm.getKey());
                            }
                            this.emePerms.put(perm.getKey(), perm.getValue());
                        }
                        for (Map.Entry<String, Boolean> perm : new HashSet<>(charPerms.entrySet())) {
                            if (this.emePerms.containsKey(perm.getKey())) {
                                if (!this.emePerms.get(perm.getKey()).equals(perm.getValue())) {
                                    continue;
                                }
                            }
                            this.emePerms.put(perm.getKey(), perm.getValue());
                        }
                        break;
                    case CHARACTER:
                        for (Map.Entry<String, Boolean> perm : new HashSet<>(charPerms.entrySet())) {
                            if (this.charPerms.containsKey(perm.getKey())) {
                                this.charPerms.remove(perm.getKey());
                            }
                            this.charPerms.put(perm.getKey(), perm.getValue());
                        }
                        for (Map.Entry<String, Boolean> perm : new HashSet<>(sgPerms.entrySet())) {
                            if (this.charPerms.containsKey(perm.getKey())) {
                                if (!this.charPerms.get(perm.getKey()).equals(perm.getValue())) {
                                    continue;
                                }
                            }
                            this.charPerms.put(perm.getKey(), perm.getValue());
                        }
                        break;
                    case SPECIALGUEST:
                        for (Map.Entry<String, Boolean> perm : new HashSet<>(sgPerms.entrySet())) {
                            if (this.sgPerms.containsKey(perm.getKey())) {
                                this.sgPerms.remove(perm.getKey());
                            }
                            this.sgPerms.put(perm.getKey(), perm.getValue());
                        }
                        for (Map.Entry<String, Boolean> perm : new HashSet<>(dvcPerms.entrySet())) {
                            if (this.sgPerms.containsKey(perm.getKey())) {
                                if (!this.sgPerms.get(perm.getKey()).equals(perm.getValue())) {
                                    continue;
                                }
                            }
                            this.sgPerms.put(perm.getKey(), perm.getValue());
                        }
                        break;
                    case DVCMEMBER:
                        for (Map.Entry<String, Boolean> perm : new HashSet<>(dvcPerms.entrySet())) {
                            if (this.dvcPerms.containsKey(perm.getKey())) {
                                this.dvcPerms.remove(perm.getKey());
                            }
                            this.dvcPerms.put(perm.getKey(), perm.getValue());
                        }
                        for (Map.Entry<String, Boolean> perm : new HashSet<>(guestPerms.entrySet())) {
                            if (this.dvcPerms.containsKey(perm.getKey())) {
                                if (!this.dvcPerms.get(perm.getKey()).equals(perm.getValue())) {
                                    continue;
                                }
                            }
                            this.dvcPerms.put(perm.getKey(), perm.getValue());
                        }
                        break;
                    case GUEST:
                        for (Map.Entry<String, Boolean> perm : new HashSet<>(guestPerms.entrySet())) {
                            if (this.guestPerms.containsKey(perm.getKey())) {
                                this.guestPerms.remove(perm.getKey());
                            }
                            this.guestPerms.put(perm.getKey(), perm.getValue());
                        }
                        break;
                    default:
                        severeShutdown();
                        break;
                }
            }
            Bukkit.getLogger().info("Permissions setup!");
        } catch (Exception e) {
            e.printStackTrace();
            severeShutdown();
        }
    }

    public void refreshPermissions(final CommandSender sender) {
        Bukkit.getScheduler().runTaskAsynchronously(MCMagicCore.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Rank rank : Rank.values()) {
                    switch (rank) {
                        case OWNER:
                            ownerPerms.clear();
                            break;
                        case MAYOR:
                            mayorPerms.clear();
                            break;
                        case MANAGER:
                            managerPerms.clear();
                            break;
                        case DEVELOPER:
                            devPerms.clear();
                            break;
                        case CASTMEMBER:
                            cmPerms.clear();
                            break;
                        case EARNINGMYEARS:
                            emePerms.clear();
                            break;
                        case CHARACTER:
                            charPerms.clear();
                            break;
                        case SPECIALGUEST:
                            sgPerms.clear();
                            break;
                        case DVCMEMBER:
                            dvcPerms.clear();
                            break;
                        case GUEST:
                            guestPerms.clear();
                            break;
                        default:
                            break;
                    }
                }
                setupPermissions();
                assignPermissions();
                sender.sendMessage(ChatColor.YELLOW + "Permissions have been refreshed!");
            }
        });
    }
}