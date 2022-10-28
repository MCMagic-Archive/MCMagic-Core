package us.mcmagic.mcmagiccore.permissions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.mcmagic.mcmagiccore.MCMagicCore;
import us.mcmagic.mcmagiccore.player.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PermSqlUtil {
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(MCMagicCore.getMCMagicConfig().sqlConnectionUrl,
                    MCMagicCore.getMCMagicConfig().sqlUser,
                    MCMagicCore.getMCMagicConfig().sqlPassword);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User createUser(UUID uuid, String name) {
        try (Connection connection = getConnection()) {
            PreparedStatement sql = connection.prepareStatement("SELECT rank,pack FROM player_data WHERE uuid=?");
            sql.setString(1, uuid.toString());
            ResultSet result = sql.executeQuery();
            if (!result.next()) {
                result.close();
                sql.close();
                return null;
            }
            Rank rank = Rank.fromString(result.getString("rank"));
            String pack = result.getString("pack");
            result.close();
            sql.close();
            PreparedStatement ach = connection.prepareStatement("SELECT * FROM achievements WHERE uuid=?");
            ach.setString(1, uuid.toString());
            ResultSet achresult = ach.executeQuery();
            List<Integer> achievements = new ArrayList<>();
            while (achresult.next()) {
                achievements.add(achresult.getInt("achid"));
            }
            achresult.close();
            ach.close();
            return new User(uuid, name, rank, pack, achievements);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Rank getRank(Player player) {
        try (Connection connection = getConnection()) {
            PreparedStatement sql = connection.prepareStatement("SELECT rank FROM player_data WHERE uuid=?");
            sql.setString(1, player.getUniqueId() + "");
            ResultSet result = sql.executeQuery();
            if (!result.next()) {
                result.close();
                sql.close();
                return Rank.GUEST;
            }
            String rank = result.getString("rank");
            result.close();
            sql.close();
            return Rank.fromString(rank);
        } catch (SQLException e) {
            e.printStackTrace();
            return Rank.GUEST;
        }
    }

    public Rank getRank(String name) {
        try (Connection connection = getConnection()) {
            PreparedStatement sql = connection.prepareStatement("SELECT rank FROM player_data WHERE username=?");
            sql.setString(1, name);
            ResultSet result = sql.executeQuery();
            if (!result.next()) {
                result.close();
                sql.close();
                return Rank.GUEST;
            }
            String rank = result.getString("rank");
            result.close();
            sql.close();
            return Rank.fromString(rank);
        } catch (SQLException e) {
            e.printStackTrace();
            return Rank.GUEST;
        }
    }

    public Rank getRank(UUID uuid) {
        try (Connection connection = getConnection()) {
            PreparedStatement sql = connection.prepareStatement("SELECT rank FROM player_data WHERE uuid=?");
            sql.setString(1, uuid.toString());
            ResultSet result = sql.executeQuery();
            if (!result.next()) {
                result.close();
                sql.close();
                return Rank.GUEST;
            }
            String rank = result.getString("rank");
            result.close();
            sql.close();
            return Rank.fromString(rank);
        } catch (SQLException e) {
            e.printStackTrace();
            return Rank.GUEST;
        }
    }

    public HashMap<String, Boolean> getPermissions(Rank rank) {
        try (Connection connection = getConnection()) {
            PreparedStatement sql = connection.prepareStatement("SELECT * FROM permissions WHERE rank=?");
            sql.setString(1, rank.getSqlName());
            ResultSet result = sql.executeQuery();
            HashMap<String, Boolean> permissions = new HashMap<>();
            while (result.next()) {
                permissions.put(result.getString("node"), result.getInt("value") == 1);
            }
            result.close();
            sql.close();
            return permissions;
        } catch (SQLException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public void setRank(UUID uuid, Rank rank) {
        try (Connection connection = getConnection()) {
            PreparedStatement sql = connection.prepareStatement("UPDATE player_data SET rank=? WHERE uuid=?");
            sql.setString(1, rank.getSqlName());
            sql.setString(2, uuid.toString());
            sql.execute();
            sql.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setPermission(String node, Rank rank, boolean value) {
        try (Connection connection = getConnection()) {
            PreparedStatement sql = connection.prepareStatement("INSERT INTO permissions values(0,?,?,?)");
            sql.setString(1, rank.getSqlName());
            sql.setString(2, node);
            sql.setInt(3, value ? 1 : 0);
            sql.execute();
            sql.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        switch (rank) {
            case OWNER:
                MCMagicCore.permManager.ownerPerms.remove(node);
                MCMagicCore.permManager.ownerPerms.put(node, value);
                break;
            case MAYOR:
                MCMagicCore.permManager.ownerPerms.remove(node);
                MCMagicCore.permManager.mayorPerms.put(node, value);
                break;
            case MANAGER:
                MCMagicCore.permManager.managerPerms.remove(node);
                MCMagicCore.permManager.managerPerms.put(node, value);
                break;
            case DEVELOPER:
                MCMagicCore.permManager.devPerms.remove(node);
                MCMagicCore.permManager.devPerms.put(node, value);
                break;
            case COORDINATOR:
                MCMagicCore.permManager.coordPerms.remove(node);
                MCMagicCore.permManager.coordPerms.put(node, value);
                break;
            case CASTMEMBER:
                MCMagicCore.permManager.cmPerms.remove(node);
                MCMagicCore.permManager.cmPerms.put(node, value);
                break;
            case EARNINGMYEARS:
                MCMagicCore.permManager.emePerms.remove(node);
                MCMagicCore.permManager.emePerms.put(node, value);
                break;
            case CHARACTER:
                MCMagicCore.permManager.charPerms.remove(node);
                MCMagicCore.permManager.charPerms.put(node, value);
                break;
            case CHARACTERGUEST:
                MCMagicCore.permManager.chargPerms.remove(node);
                MCMagicCore.permManager.chargPerms.put(node, value);
                break;
            case SPECIALGUEST:
                MCMagicCore.permManager.sgPerms.remove(node);
                MCMagicCore.permManager.sgPerms.put(node, value);
                break;
            case MINEDISNEY:
                MCMagicCore.permManager.sgPerms.remove(node);
                MCMagicCore.permManager.sgPerms.put(node, value);
                break;
            case CRAFTVENTURE:
                MCMagicCore.permManager.sgPerms.remove(node);
                MCMagicCore.permManager.sgPerms.put(node, value);
                break;
            case MAGICALDREAMS:
                MCMagicCore.permManager.sgPerms.remove(node);
                MCMagicCore.permManager.sgPerms.put(node, value);
                break;
            case ADVENTURERIDGE:
                MCMagicCore.permManager.sgPerms.remove(node);
                MCMagicCore.permManager.sgPerms.put(node, value);
                break;
            case ANCHORNETWORK:
                MCMagicCore.permManager.sgPerms.remove(node);
                MCMagicCore.permManager.sgPerms.put(node, value);
                break;
            case DVCMEMBER:
                MCMagicCore.permManager.dvcPerms.remove(node);
                MCMagicCore.permManager.dvcPerms.put(node, value);
                break;
            case GUEST:
                MCMagicCore.permManager.guestPerms.remove(node);
                MCMagicCore.permManager.guestPerms.put(node, value);
                break;
            default:
                MCMagicCore.permManager.guestPerms.remove(node);
                MCMagicCore.permManager.guestPerms.put(node, value);
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.recalculatePermissions();
        }
    }

    public void unsetPermission(String node, Rank rank) {
        try (Connection connection = getConnection()) {
            PreparedStatement sql = connection.prepareStatement("DELETE FROM permissions WHERE rank=? AND node=?");
            sql.setString(1, rank.getSqlName());
            sql.setString(2, node);
            sql.execute();
            sql.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        switch (rank) {
            case OWNER:
                MCMagicCore.permManager.ownerPerms.remove(node);
                break;
            case MAYOR:
                MCMagicCore.permManager.mayorPerms.remove(node);
                break;
            case MANAGER:
                MCMagicCore.permManager.managerPerms.remove(node);
                break;
            case DEVELOPER:
                MCMagicCore.permManager.devPerms.remove(node);
                break;
            case COORDINATOR:
                MCMagicCore.permManager.coordPerms.remove(node);
                break;
            case CASTMEMBER:
                MCMagicCore.permManager.cmPerms.remove(node);
                break;
            case EARNINGMYEARS:
                MCMagicCore.permManager.emePerms.remove(node);
                break;
            case CHARACTER:
                MCMagicCore.permManager.charPerms.remove(node);
                break;
            case CHARACTERGUEST:
                MCMagicCore.permManager.chargPerms.remove(node);
                break;
            case SPECIALGUEST:
                MCMagicCore.permManager.sgPerms.remove(node);
                break;
            case MINEDISNEY:
                MCMagicCore.permManager.sgPerms.remove(node);
                break;
            case CRAFTVENTURE:
                MCMagicCore.permManager.sgPerms.remove(node);
                break;
            case MAGICALDREAMS:
                MCMagicCore.permManager.sgPerms.remove(node);
                break;
            case ADVENTURERIDGE:
                MCMagicCore.permManager.sgPerms.remove(node);
                break;
            case ANCHORNETWORK:
                MCMagicCore.permManager.sgPerms.remove(node);
                break;
            case DVCMEMBER:
                MCMagicCore.permManager.dvcPerms.remove(node);
                break;
            case GUEST:
                MCMagicCore.permManager.guestPerms.remove(node);
                break;
            default:
                MCMagicCore.permManager.guestPerms.remove(node);
        }
    }

    public boolean playerExists(String name) {
        try (Connection connection = getConnection()) {
            PreparedStatement sql = connection.prepareStatement("SELECT id FROM player_data WHERE username=?");
            sql.setString(1, name);
            ResultSet result = sql.executeQuery();
            boolean contains = result.next();
            result.close();
            sql.close();
            return contains;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getMembers(Rank rank) {
        try (Connection connection = getConnection()) {
            List<String> list = new ArrayList<>();
            PreparedStatement sql = connection.prepareStatement("SELECT username FROM player_data WHERE rank=?");
            sql.setString(1, rank.getSqlName());
            ResultSet result = sql.executeQuery();
            while (result.next()) {
                list.add(result.getString("username"));
            }
            result.close();
            sql.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<UUID> getMemberUniqueIds(Rank rank) {
        try (Connection connection = getConnection()) {
            List<UUID> list = new ArrayList<>();
            PreparedStatement sql = connection.prepareStatement("SELECT uuid FROM player_data WHERE rank=?");
            sql.setString(1, rank.getSqlName());
            ResultSet result = sql.executeQuery();
            while (result.next()) {
                list.add(UUID.fromString(result.getString("uuid")));
            }
            result.close();
            sql.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}