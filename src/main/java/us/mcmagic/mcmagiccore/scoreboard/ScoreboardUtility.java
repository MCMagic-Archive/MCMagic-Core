package us.mcmagic.mcmagiccore.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scoreboard.*;
import us.mcmagic.mcmagiccore.MCMagicCore;
import us.mcmagic.mcmagiccore.permissions.Rank;
import us.mcmagic.mcmagiccore.player.User;

import java.text.NumberFormat;
import java.util.*;

/**
 * Created by Marc on 11/18/14
 */
public class ScoreboardUtility {
    public static ScoreboardManager sbm = Bukkit.getScoreboardManager();
    private static List<UUID> toggledTags = new ArrayList<>();
    private static HashMap<UUID, Scoreboard> needsSet = new HashMap<>();
    private static int onlineCount = 0;

    public static void start() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(MCMagicCore.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, Scoreboard> entry : new HashSet<>(needsSet.entrySet())) {
                    UUID uuid = entry.getKey();
                    Scoreboard sb = needsSet.remove(uuid);
                    Player player = Bukkit.getPlayer(uuid);
                    User user = MCMagicCore.getUser(uuid);
                    if (player == null || user == null) {
                        continue;
                    }
                    updateData(player);
                    if (MCMagicCore.getMCMagicConfig().informationSidebarEnabled) {
                        int bal = player.getMetadata("bal").get(0).asInt();
                        int token = player.getMetadata("tokens").get(0).asInt();
                        Objective stats = sb.registerNewObjective("MCMagic", "Main");
                        stats.setDisplayName(ChatColor.AQUA + "MCMagic" + ChatColor.GREEN + ".us Servers");
                        Score bals;
                        if (bal > 2147483646) {
                            bals = stats.getScore(ChatColor.GREEN + "$ " + ChatColor.GREEN + 2147483646 + "+");
                        } else {
                            bals = stats.getScore(ChatColor.GREEN + "$ " + ChatColor.GREEN + bal);
                        }
                        Score tokens;
                        if (token > 2147483646) {
                            tokens = stats.getScore(ChatColor.YELLOW + "✪ " + 2147483646 + "+");
                        } else {
                            tokens = stats.getScore(ChatColor.YELLOW + "✪ " + token);
                        }
                        Score rank = stats.getScore(ChatColor.GREEN + "Rank: " + user.getRank().getTagColor() +
                                user.getRank().getName());
                        Score online = stats.getScore(ChatColor.GREEN + "Online Guests: " + format(onlineCount));
                        Score server = stats.getScore(ChatColor.GREEN + MCMagicCore.getMCMagicConfig().pos + ": " +
                                MCMagicCore.getMCMagicConfig().serverName);
                        Score blank1 = stats.getScore(" ");
                        Score blank2 = stats.getScore("  ");
                        Score blank3 = stats.getScore("   ");
                        Score blank4 = stats.getScore("    ");
                        blank4.setScore(8);
                        bals.setScore(7);
                        blank3.setScore(6);
                        tokens.setScore(5);
                        blank2.setScore(4);
                        rank.setScore(3);
                        blank1.setScore(2);
                        online.setScore(1);
                        server.setScore(0);
                        stats.setDisplaySlot(DisplaySlot.SIDEBAR);
                    }
                    if (MCMagicCore.getMCMagicConfig().rankTabEnabled) {
                        Team owner = sb.registerNewTeam("owner");
                        Team mayor = sb.registerNewTeam("mayor");
                        Team manager = sb.registerNewTeam("manager");
                        Team dev = sb.registerNewTeam("dev");
                        Team coordinator = sb.registerNewTeam("coordinator");
                        Team cm = sb.registerNewTeam("cm");
                        Team eme = sb.registerNewTeam("eme");
                        Team character = sb.registerNewTeam("character");
                        Team sg = sb.registerNewTeam("specialguest");
                        Team mcpro = sb.registerNewTeam("mcprohosting");
                        Team partner = sb.registerNewTeam("partner");
                        Team share = sb.registerNewTeam("shareholder");
                        Team dvc = sb.registerNewTeam("dvc");
                        Team guest = sb.registerNewTeam("guest");
                        owner.setPrefix(trans("&6[Owner] "));
                        mayor.setPrefix(trans("&6[Mayor] "));
                        manager.setPrefix(trans("&6[Manager] "));
                        dev.setPrefix(trans("&6[Developer] "));
                        coordinator.setPrefix(trans("&a[Coordinator] "));
                        cm.setPrefix(trans("&a[Cast Member] "));
                        eme.setPrefix(trans("&a[EME] "));
                        character.setPrefix(trans("&9[Character] "));
                        sg.setPrefix(trans("&5[SG] "));
                        mcpro.setPrefix(trans("&c[MCPro] "));
                        partner.setPrefix(trans("&5[Partner] "));
                        share.setPrefix(trans("&d[Shareholder] "));
                        dvc.setPrefix(trans("&b[DVC] "));
                        guest.setPrefix(ChatColor.GRAY + "");
                        String playerTeam = "";
                        switch (user.getRank()) {
                            case OWNER:
                                playerTeam = "owner";
                                break;
                            case MAYOR:
                                playerTeam = "mayor";
                                break;
                            case MANAGER:
                                playerTeam = "manager";
                                break;
                            case DEVELOPER:
                                playerTeam = "dev";
                                break;
                            case COORDINATOR:
                                playerTeam = "coordinator";
                                break;
                            case CASTMEMBER:
                                playerTeam = "cm";
                                break;
                            case EARNINGMYEARS:
                                playerTeam = "eme";
                                break;
                            case CHARACTER:
                                playerTeam = "character";
                                break;
                            case CHARACTERGUEST:
                                playerTeam = "character";
                                break;
                            case SPECIALGUEST:
                                playerTeam = "specialguest";
                                break;
                            case MCPROHOSTING:
                                playerTeam = "mcprohosting";
                                break;
                            case MINEDISNEY:
                                playerTeam = "partner";
                                break;
                            case CRAFTVENTURE:
                                playerTeam = "partner";
                                break;
                            case MAGICALDREAMS:
                                playerTeam = "partner";
                                break;
                            case ADVENTURERIDGE:
                                playerTeam = "partner";
                                break;
                            case ANCHORNETWORK:
                                playerTeam = "partner";
                                break;
                            case SHAREHOLDER:
                                playerTeam = "shareholder";
                                break;
                            case DVCMEMBER:
                                playerTeam = "dvc";
                                break;
                            case GUEST:
                                playerTeam = "guest";
                                break;
                            default:
                                playerTeam = "guest";
                                break;
                        }
                        for (Player tp : Bukkit.getOnlinePlayers()) {
                            User tpu = MCMagicCore.getUser(tp.getUniqueId());
                            if (tpu == null) {
                                continue;
                            }
                            Rank rank = tpu.getRank();
                            switch (rank) {
                                case OWNER:
                                    owner.addEntry(tp.getName());
                                    break;
                                case MAYOR:
                                    mayor.addEntry(tp.getName());
                                    break;
                                case MANAGER:
                                    manager.addEntry(tp.getName());
                                    break;
                                case DEVELOPER:
                                    dev.addEntry(tp.getName());
                                    break;
                                case COORDINATOR:
                                    coordinator.addEntry(tp.getName());
                                    break;
                                case CASTMEMBER:
                                    cm.addEntry(tp.getName());
                                    break;
                                case EARNINGMYEARS:
                                    eme.addEntry(tp.getName());
                                    break;
                                case CHARACTER:
                                    character.addEntry(tp.getName());
                                    break;
                                case CHARACTERGUEST:
                                    character.addEntry(tp.getName());
                                    break;
                                case SPECIALGUEST:
                                    sg.addEntry(tp.getName());
                                    break;
                                case MCPROHOSTING:
                                    mcpro.addEntry(tp.getName());
                                    break;
                                case MINEDISNEY:
                                    partner.addEntry(tp.getName());
                                    break;
                                case CRAFTVENTURE:
                                    partner.addEntry(tp.getName());
                                    break;
                                case MAGICALDREAMS:
                                    partner.addEntry(tp.getName());
                                    break;
                                case ADVENTURERIDGE:
                                    partner.addEntry(tp.getName());
                                    break;
                                case ANCHORNETWORK:
                                    partner.addEntry(tp.getName());
                                    break;
                                case SHAREHOLDER:
                                    share.addEntry(tp.getName());
                                    break;
                                case DVCMEMBER:
                                    dvc.addEntry(tp.getName());
                                    break;
                                case GUEST:
                                    guest.addEntry(tp.getName());
                                    break;
                                default:
                                    guest.addEntry(tp.getName());
                                    break;
                            }
                            if (tp.getUniqueId().equals(player.getUniqueId())) {
                                continue;
                            }
                            try {
                                tp.getScoreboard().getTeam(playerTeam).addEntry(player.getName());
                            } catch (Exception ignored) {
                            }
                        }
                    }
                    player.setScoreboard(sb);
                }
            }

            private String trans(String s) {
                return ChatColor.translateAlternateColorCodes('&', s);
            }


        }, 0L, 10L);
    }

    public static void setDefaultScoreboard(Player player) {
        Scoreboard sb = sbm.getNewScoreboard();
        needsSet.put(player.getUniqueId(), sb);
    }

    public static void updateData(Player player) {
        int bal = MCMagicCore.economy.getBalance(player.getUniqueId());
        int tokens = MCMagicCore.economy.getTokens(player.getUniqueId());
        player.setMetadata("bal", new FixedMetadataValue(MCMagicCore.getInstance(), bal));
        player.setMetadata("tokens", new FixedMetadataValue(MCMagicCore.getInstance(), tokens));
    }

    public static void logout(UUID uuid, String name) {
        User user = MCMagicCore.getUser(uuid);
        if (user == null) {
            return;
        }
        Rank r = user.getRank();
        for (Player tp : Bukkit.getOnlinePlayers()) {
            if (tp.getUniqueId().equals(uuid)) {
                continue;
            }
            try {
                tp.getScoreboard().getTeam(r.name().toLowerCase()).removeEntry(name);
            } catch (Exception ignored) {
            }
        }
        toggledTags.remove(uuid);
    }

    public static void toggleTags(Player player) {
        Scoreboard sb = player.getScoreboard();
        if (toggledTags.contains(player.getUniqueId())) {
            toggledTags.remove(player.getUniqueId());
            for (Team team : sb.getTeams()) {
                team.setNameTagVisibility(NameTagVisibility.ALWAYS);
            }
            return;
        }
        toggledTags.add(player.getUniqueId());
        for (Team team : sb.getTeams()) {
            team.setNameTagVisibility(NameTagVisibility.NEVER);
        }
    }

    public static void update(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return;
        }
        if (!MCMagicCore.getMCMagicConfig().informationSidebarEnabled) {
            return;
        }
        Scoreboard sb = player.getScoreboard();
        Objective stats = sb.getObjective(DisplaySlot.SIDEBAR);
        int bal = player.getMetadata("bal").get(0).asInt();
        int newbal = MCMagicCore.economy.getBalance(player.getUniqueId());
        if (bal != newbal) {
            if (bal > 2147483646) {
                sb.resetScores(ChatColor.GREEN + "$ " + ChatColor.GREEN + 2147483646 + "+");
            } else {
                sb.resetScores(ChatColor.GREEN + "$ " + ChatColor.GREEN + bal);
            }
            Score nbal;
            if (newbal > 2147483646) {
                nbal = stats.getScore(ChatColor.GREEN + "$ " + ChatColor.GREEN + 2147483646 + "+");
            } else {
                nbal = stats.getScore(ChatColor.GREEN + "$ " + ChatColor.GREEN + newbal);
            }
            nbal.setScore(7);
        }
        int token = player.getMetadata("tokens").get(0).asInt();
        int newtoken = MCMagicCore.economy.getTokens(player.getUniqueId());
        if (token != newtoken) {
            if (token > 2147483646) {
                sb.resetScores(ChatColor.YELLOW + "✪ " + 2147483646 + "+");
            } else {
                sb.resetScores(ChatColor.YELLOW + "✪ " + token);
            }
            Score tokens;
            if (newtoken > 2147483646) {
                tokens = stats.getScore(ChatColor.YELLOW + "✪ " + 2147483646 + "+");
            } else {
                tokens = stats.getScore(ChatColor.YELLOW + "✪ " + newtoken);
            }
            tokens.setScore(5);
        }
        updateData(player);
    }

    public static void setOnlineCount(int count) {
        if (!MCMagicCore.getMCMagicConfig().informationSidebarEnabled) {
            return;
        }
        if (onlineCount == count) {
            return;
        }
        final int previous = onlineCount;
        onlineCount = count;
        for (Player tp : Bukkit.getOnlinePlayers()) {
            Scoreboard sb = tp.getScoreboard();
            Objective obj = sb.getObjective(DisplaySlot.SIDEBAR);
            if (obj == null) {
                continue;
            }
            sb.resetScores(ChatColor.GREEN + "Online Guests: " + format(previous));
            obj.getScore(ChatColor.GREEN + "Online Guests: " + format(count)).setScore(1);
        }
    }

    private static String format(int i) {
        return NumberFormat.getNumberInstance(Locale.US).format(i);
    }
}