package us.mcmagic.mcmagiccore.arcade;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.mcmagic.mcmagiccore.MCMagicCore;
import us.mcmagic.mcmagiccore.permissions.Rank;
import us.mcmagic.mcmagiccore.player.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Marc on 2/24/15
 */
public class GameManager {
    private ChatColor primary = ChatColor.BLUE;
    private ChatColor secondary = ChatColor.GREEN;
    private ChatColor leave = ChatColor.RED;
    private String header = "" + ChatColor.GREEN + "" + ChatColor.BOLD +
            "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬";
    private String gameNameHeader;
    private String footer = "" + ChatColor.GREEN + "" + ChatColor.BOLD +
            "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬";
    private String[] description;
    private String gameName;
    private int startAmount;
    private int maxAmount;
    private int countdown;
    private boolean gameStarted = false;
    private boolean counting = false;
    private int taskID = 0;

    public void setGameData(String gameName, String gameNameHeader, String[] description, int startAmount,
                            int maxAmount, int countdown) {
        this.gameName = gameName;
        this.gameNameHeader = ChatColor.BLUE + gameNameHeader;
        this.description = description;
        this.startAmount = startAmount;
        this.maxAmount = maxAmount;
        this.countdown = countdown;
    }

    public void gameStart() {
        gameStarted = true;
        List<String> msgs = new ArrayList<>();
        msgs.add(header);
        msgs.add("    " + gameNameHeader);
        msgs.add(" ");
        for (String s : description) {
            msgs.add("    " + s);
        }
        msgs.add(" ");
        msgs.add(footer);
        Bukkit.getPluginManager().callEvent(new GameStartEvent());
        for (String s : msgs) {
            Bukkit.broadcastMessage(s);
        }
    }

    public void broadcast(String message) {
        Bukkit.broadcastMessage(ChatColor.WHITE + "[" + primary + gameName + ChatColor.WHITE + "] " + secondary +
                message);
    }

    public void join(Player player) {
        if (gameStarted) {
            return;
        }
        User user = MCMagicCore.getUser(player.getUniqueId());
        if (user == null) {
            Bukkit.broadcastMessage(ChatColor.WHITE + "[" + primary + gameName + ChatColor.WHITE + "] " +
                    ChatColor.GRAY + player.getName() + secondary + " has joined! " + ChatColor.LIGHT_PURPLE + "(" +
                    Bukkit.getOnlinePlayers().size() + "/" + maxAmount + ")");
        } else {
            Rank rank = user.getRank();
            ChatColor color = rank == Rank.GUEST ? ChatColor.GRAY : rank.getTagColor();
            Bukkit.broadcastMessage(ChatColor.WHITE + "[" + primary + gameName + ChatColor.WHITE + "] " + color +
                    player.getName() + secondary + " has joined! " + ChatColor.LIGHT_PURPLE + "(" +
                    Bukkit.getOnlinePlayers().size() + "/" + maxAmount + ")");
        }
        if (Bukkit.getOnlinePlayers().size() >= startAmount && !counting) {
            startCountdown();
        }
    }

    public void startCountdown() {
        counting = true;
        taskID = Bukkit.getScheduler().runTaskTimer(MCMagicCore.getInstance(), new Runnable() {
            int i = countdown;

            @Override
            public void run() {
                if (i == 0) {
                    stopCountdown(false);
                    gameStart();
                    return;
                }
                if (shouldBroadcast(i)) {
                    Bukkit.getPluginManager().callEvent(new GameCountdownEvent(i));
                    broadcast(ChatColor.YELLOW + "" + i + ChatColor.GREEN + " seconds until the game starts!");
                }
                i--;
            }
        }, 0L, 20L).getTaskId();
    }

    private boolean shouldBroadcast(int i) {
        return i <= 10 || i % 15 == 0;
    }

    public void moneyMessage(Player player, int amount) {
        List<String> msgs = new ArrayList<>();
        msgs.add(header);
        msgs.add("                            " + ChatColor.GREEN + "" + ChatColor.BOLD + "Money Rewards");
        msgs.add(" ");
        msgs.add("                             " + ChatColor.YELLOW + "You earned " + ChatColor.GREEN + "$" + amount);
        msgs.add(" ");
        msgs.add(footer);
        for (String s : msgs) {
            player.sendMessage(s);
        }
    }

    private void stopCountdown(boolean announce) {
        Bukkit.getScheduler().cancelTask(taskID);
        counting = false;
        if (announce) {
            broadcast("Start-up cancelled, not enough players!");
        }
    }

    public void message(Player player, String message) {
        player.sendMessage(ChatColor.WHITE + "[" + primary + gameName + ChatColor.WHITE + "] " + secondary + message);
    }

    public void quit(Player player) {
        if (gameStarted) {
            return;
        }
        if (counting && Bukkit.getOnlinePlayers().size() < startAmount) {
            stopCountdown(true);
        }
        User user = MCMagicCore.getUser(player.getUniqueId());
        if (user == null) {
            Bukkit.broadcastMessage(ChatColor.WHITE + "[" + primary + gameName + ChatColor.WHITE + "] " +
                    ChatColor.GRAY + player.getName() + ChatColor.RED + " has quit!");
            return;
        }
        Rank rank = user.getRank();
        ChatColor color = rank == Rank.GUEST ? ChatColor.GRAY : rank.getTagColor();
        Bukkit.broadcastMessage(ChatColor.WHITE + "[" + primary + gameName + ChatColor.WHITE + "] " + color +
                player.getName() + ChatColor.RED + " has quit!");
    }

    public void setPlayerCount(String serverName, int count) {
        try (Connection connection = MCMagicCore.permSqlUtil.getConnection()) {
            PreparedStatement sql = connection.prepareStatement("UPDATE game_info SET playercount=? WHERE servername=?");
            sql.setInt(1, count);
            sql.setString(2, serverName);
            sql.execute();
            sql.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setState(String serverName, ServerState state) {
        Integer[] array = state.getArray();
        int o = array[0];
        int r = array[1];
        int i = array[2];
        int s = array[3];
        try (Connection connection = MCMagicCore.permSqlUtil.getConnection()) {
            PreparedStatement sql = connection.prepareStatement("UPDATE game_info SET online=?,rebooting=?,ingame=?," +
                    "spectating=? WHERE servername=?");
            sql.setInt(1, o);
            sql.setInt(2, r);
            sql.setInt(3, i);
            sql.setInt(4, s);
            sql.setString(5, serverName);
            sql.execute();
            sql.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public GameData getGameData(UUID uuid, String game) {
        try (Connection connection = MCMagicCore.permSqlUtil.getConnection()) {
            PreparedStatement sql = connection.prepareStatement("SELECT * FROM " + game + "_data WHERE uuid=?");
            sql.setString(1, uuid.toString());
            ResultSet result = sql.executeQuery();
            if (!result.next()) {
                result.close();
                sql.close();
                return createData(uuid, game);
            }
            ResultSetMetaData md = result.getMetaData();
            HashMap<String, Integer> ints = new HashMap<>();
            HashMap<String, String> strings = new HashMap<>();
            for (int i = 3; i < md.getColumnCount() + 1; i++) {
                String name = md.getColumnName(i);
                switch (md.getColumnType(i)) {
                    case Types.INTEGER:
                        ints.put(name, result.getInt(i));
                        break;
                    case Types.VARCHAR:
                        strings.put(name, result.getString(i));
                        break;
                }
            }
            GameData data = new GameData(uuid, ints, strings);
            result.close();
            sql.close();
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private GameData createData(UUID uuid, String game) {
        try (Connection connection = MCMagicCore.permSqlUtil.getConnection()) {
            PreparedStatement sql = connection.prepareStatement("INSERT INTO " + game + "_data ('uuid') VALUES (" +
                    uuid.toString() + ")");
            sql.execute();
            sql.close();
            PreparedStatement sel = connection.prepareStatement("SELECT * FROM " + game + "_data WHERE uuid=?");
            sel.setString(1, uuid.toString());
            ResultSet result = sel.executeQuery();
            if (!result.next()) {
                result.close();
                sel.close();
                return null;
            }
            ResultSetMetaData md = result.getMetaData();
            HashMap<String, Integer> ints = new HashMap<>();
            HashMap<String, String> strings = new HashMap<>();
            for (int i = 3; i < md.getColumnCount() + 1; i++) {
                String name = md.getColumnName(i);
                switch (md.getColumnType(i)) {
                    case Types.INTEGER:
                        ints.put(name, result.getInt(i));
                        break;
                    case Types.VARCHAR:
                        strings.put(name, result.getString(i));
                        break;
                }
            }
            GameData data = new GameData(uuid, ints, strings);
            result.close();
            sel.close();
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void changeValue(Player player, String gname, int newValue) {
        try (Connection connection = MCMagicCore.permSqlUtil.getConnection()) {
            PreparedStatement sql = connection.prepareStatement("UPDATE leaderboard SET " + gname + "=? WHERE uuid=?");
            sql.setInt(1, newValue);
            sql.setString(2, player.getUniqueId().toString());
            sql.execute();
            sql.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getValue(Player player, String game) {
        try (Connection connection = MCMagicCore.permSqlUtil.getConnection()) {
            PreparedStatement sql = connection.prepareStatement("SELECT " + game + " FROM leaderboard WHERE uuid=?");
            sql.setString(1, player.getUniqueId().toString());
            ResultSet result = sql.executeQuery();
            if (!result.next()) {
                result.close();
                sql.close();
                return 0;
            }
            int value = result.getInt(game);
            result.close();
            sql.close();
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}