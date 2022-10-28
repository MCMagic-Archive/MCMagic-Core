package us.mcmagic.mcmagiccore.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import us.mcmagic.mcmagiccore.MCMagicCore;
import us.mcmagic.mcmagiccore.achievements.MagicAchievement;
import us.mcmagic.mcmagiccore.permissions.Rank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Created by Marc on 11/18/14
 */
public class User {
    private UUID uuid;
    private String name;
    private Rank rank;
    private String preferredPack;
    private String resourcePack = "unknown";
    private String textureHash;
    private List<Integer> achievements;

    public User(UUID uuid, String name, Rank rank, String preferredPack, List<Integer> achievements) {
        this.uuid = uuid;
        this.name = name;
        this.rank = rank;
        this.preferredPack = preferredPack;
        this.achievements = achievements;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public String getPreferredPack() {
        return preferredPack;
    }

    public void setPreferredPack(String preferredPack) {
        this.preferredPack = preferredPack;
    }

    @Deprecated
    public String getCurrentPack() {
        return resourcePack;
    }

    @Deprecated
    public void setCurrentPack(String resourcePack) {
        this.resourcePack = resourcePack;
    }

    public String getResourcePack() {
        return resourcePack;
    }

    public void setResourcePack(String resourcePack) {
        this.resourcePack = resourcePack;
    }

    public String getTextureHash() {
        return textureHash;
    }

    public void setTextureHash(String textureHash) {
        this.textureHash = textureHash;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public List<Integer> getAchievements() {
        return achievements;
    }

    public boolean hasAchievement(int i) {
        return achievements.contains(i);
    }

    public void giveAchievement(final int i) {
        if (hasAchievement(i)) {
            return;
        }
        achievements.add(i);
        MagicAchievement ach = MCMagicCore.achievementManager.getAchievement(i);
        Player p = Bukkit.getPlayer(uuid);
        p.sendMessage(ChatColor.GREEN + "--------------" + ChatColor.GOLD + "" + ChatColor.BOLD + "Achievement" +
                ChatColor.GREEN + "--------------\n" + ChatColor.AQUA + ach.getDisplayName() + "\n" + ChatColor.GRAY +
                "" + ChatColor.ITALIC + ach.getDescription() + ChatColor.GREEN + "\n----------------------------------------");
        p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 100f, 0.75f);
        Bukkit.getScheduler().runTaskAsynchronously(MCMagicCore.getInstance(), new Runnable() {
            @Override
            public void run() {
                try (Connection connection = MCMagicCore.permSqlUtil.getConnection()) {
                    PreparedStatement sql = connection.prepareStatement("INSERT INTO achievements (uuid, achid, time) VALUES (?,?,?)");
                    sql.setString(1, getUniqueId().toString());
                    sql.setInt(2, i);
                    sql.setInt(3, (int) (System.currentTimeMillis() / 1000));
                    sql.execute();
                    sql.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        MCMagicCore.economy.addTokens(p.getUniqueId(), 5, "Achievement ID " + i);
    }
}