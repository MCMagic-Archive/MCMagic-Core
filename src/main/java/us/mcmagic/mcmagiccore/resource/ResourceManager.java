package us.mcmagic.mcmagiccore.resource;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import net.minecraft.server.v1_8_R3.PacketPlayOutResourcePackSend;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import us.mcmagic.mcmagiccore.MCMagicCore;
import us.mcmagic.mcmagiccore.dashboard.packets.dashboard.PacketGetPack;
import us.mcmagic.mcmagiccore.dashboard.packets.dashboard.PacketSetPack;
import us.mcmagic.mcmagiccore.player.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Marc on 3/17/15
 */
public class ResourceManager {
    private HashMap<String, ResourcePack> packs = new HashMap<>();
    private boolean first = true;
    private HashMap<UUID, String> downloading = new HashMap<>();

    public void login(final User user) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(MCMagicCore.getInstance(), new Runnable() {
            @Override
            public void run() {
                Player tp = Bukkit.getPlayer(user.getUniqueId());
                if (tp == null) {
                    return;
                }
                PacketGetPack packet = new PacketGetPack(tp.getUniqueId(), "");
                MCMagicCore.dashboardConnection.send(packet);
            }
        }, 10L);
    }

    public void initialize() throws SQLException {
        packs.clear();
        Connection connection = MCMagicCore.permSqlUtil.getConnection();
        PreparedStatement sql = connection.prepareStatement("SELECT * FROM resource_packs");
        ResultSet result = sql.executeQuery();
        while (result.next()) {
            packs.put(result.getString("name"), new ResourcePack(result.getString("name"), result.getString("url")));
        }
        result.close();
        sql.close();
        if (first) {
            ProtocolLibrary.getProtocolManager().addPacketListener(new ResourceListener(MCMagicCore.getInstance(),
                    PacketType.Play.Client.RESOURCE_PACK_STATUS));
            first = false;
        }
    }

    public void downloadingResult(UUID uuid, PackStatus status) {
        if (status == null) {
            downloading.remove(uuid);
            return;
        }
        switch (status) {
            case LOADED:
                String pack = downloading.remove(uuid);
                User user = MCMagicCore.getUser(uuid);
                user.setResourcePack(pack);
                setCurrentPack(user, pack);
                break;
            case FAILED:
            case DECLINED:
                downloading.remove(uuid);
                break;
        }
    }

    public List<ResourcePack> getPacks() {
        return new ArrayList<>(packs.values());
    }

    public ResourcePack getPack(String name) {
        return packs.get(name);
    }

    public void setPreferredPack(UUID uuid, String name) {
        MCMagicCore.getUser(uuid).setPreferredPack(name);
        try (Connection connection = MCMagicCore.permSqlUtil.getConnection()) {
            PreparedStatement sql = connection.prepareStatement("UPDATE player_data SET pack=? WHERE uuid=?");
            sql.setString(1, name);
            sql.setString(2, uuid.toString());
            sql.execute();
            sql.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendPack(Player player, ResourcePack pack) {
        User user = MCMagicCore.getUser(player.getUniqueId());
        player.sendMessage(ChatColor.GREEN + "Attempting to send you the " + ChatColor.YELLOW + pack.getName() +
                ChatColor.GREEN + " Resource Pack!");
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutResourcePackSend(pack.getUrl(),
                "null"));
        downloading.put(player.getUniqueId(), pack.getName());

    }

    public void setCurrentPack(User user, String pack) {
        PacketSetPack packet = new PacketSetPack(user.getUniqueId(), pack);
        MCMagicCore.dashboardConnection.send(packet);
    }

    public void sendPack(Player player, String name) {
        ResourcePack pack = getPack(name);
        if (pack == null) {
            player.sendMessage(ChatColor.RED + "We tried to send you a Resource Pack, but it was not found!");
            player.sendMessage(ChatColor.RED + "Please contact a Cast Member about this. (Error Code 101)");
            return;
        }
        sendPack(player, pack);
    }

    public void reload() {
        packs.clear();
        try {
            initialize();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
