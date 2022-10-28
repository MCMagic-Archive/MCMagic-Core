package us.mcmagic.mcmagiccore.listeners;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.permissions.PermissionAttachment;
import us.mcmagic.mcmagiccore.MCMagicCore;
import us.mcmagic.mcmagiccore.permissions.PermManager;
import us.mcmagic.mcmagiccore.permissions.Rank;
import us.mcmagic.mcmagiccore.player.User;
import us.mcmagic.mcmagiccore.scoreboard.ScoreboardUtility;
import us.mcmagic.mcmagiccore.title.TabTitleObject;
import us.mcmagic.mcmagiccore.title.TitleObject;

import java.io.File;
import java.util.UUID;

public class PlayerJoinAndLeave implements Listener {
    private static TitleObject title;
    private static TabTitleObject tab;

    public static void initialize() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/MCMagicCore/config.yml"));
        if (MCMagicCore.getMCMagicConfig().titleEnabled) {
            title = new TitleObject(ChatColor.translateAlternateColorCodes('&',
                    config.getString("title.join.top")), ChatColor.translateAlternateColorCodes('&',
                    config.getString("title.join.bottom"))).setFadeIn(config.getInt("title.fade.in"))
                    .setStay(config.getInt("title.stay")).setFadeOut(config.getInt("title.fade.out"));
        }
        if (config.getBoolean("tab.enabled")) {
            tab = new TabTitleObject(ChatColor.translateAlternateColorCodes('&', config
                    .getString("tab.header")), ChatColor.translateAlternateColorCodes('&',
                    config.getString("tab.footer")));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncJoin(AsyncPlayerPreLoginEvent event) {
        try {
            UUID uuid = event.getUniqueId();
            MCMagicCore.deleteUser(uuid);
            MCMagicCore.addUser(PermManager.createUser(uuid, event.getName()));
        } catch (Exception e) {
            e.printStackTrace();
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED +
                    "An error occurred while you were logging in, please rejoin!");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncJoinFail(AsyncPlayerPreLoginEvent event) {
        if (!event.getLoginResult().equals(AsyncPlayerPreLoginEvent.Result.ALLOWED)) {
            MCMagicCore.permManager.onQuit(event.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (MCMagicCore.serverStarting) {
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            event.setKickMessage("This server is still starting up!");
            MCMagicCore.permManager.onQuit(event.getPlayer(), false);
            return;
        }
        if (event.getResult().equals(PlayerLoginEvent.Result.KICK_WHITELIST)) {
            User user = MCMagicCore.getUser(event.getPlayer().getUniqueId());
            if (user.getRank().getRankId() >= Rank.CASTMEMBER.getRankId()) {
                event.setResult(PlayerLoginEvent.Result.ALLOWED);
                return;
            }
        }
        if (!event.getResult().equals(PlayerLoginEvent.Result.ALLOWED)) {
            MCMagicCore.permManager.onQuit(event.getPlayer(), false);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage("");
        Player player = event.getPlayer();
        User user = MCMagicCore.getUser(player.getUniqueId());
        boolean op = user.getRank().getOp();
        if (player.isOp() != op) {
            player.setOp(op);
        }
        PermissionAttachment attachment = player.addAttachment(MCMagicCore.getInstance());
        MCMagicCore.resourceManager.login(user);
        MCMagicCore.permManager.setPermissions(player, user.getRank(), attachment);
        Rank rank = user.getRank();
        if (MCMagicCore.getMCMagicConfig().informationSidebarEnabled ||
                MCMagicCore.getMCMagicConfig().rankTabEnabled) {
            ScoreboardUtility.setDefaultScoreboard(player);
        }
        if (MCMagicCore.getMCMagicConfig().titleEnabled) {
            sendTitle(player);
        }
        if (MCMagicCore.getMCMagicConfig().tabEnabled) {
            sendTab(player);
        }
        try {
            user.setTextureHash(((CraftPlayer) player).getHandle().getProfile().getProperties().get("textures").iterator()
                    .next().getValue());
        } catch (Exception ignored) {
        }
        if (MCMagicCore.getMCMagicConfig().gameServer) {
            MCMagicCore.gameManager.join(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage("");
        Player player = event.getPlayer();
        if (MCMagicCore.getMCMagicConfig().gameServer) {
            MCMagicCore.gameManager.quit(player);
        }
        MCMagicCore.permManager.onQuit(player, true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event) {
        event.setLeaveMessage("");
        Player player = event.getPlayer();
        if (MCMagicCore.getMCMagicConfig().gameServer) {
            MCMagicCore.gameManager.quit(player);
        }
        MCMagicCore.permManager.onQuit(player, true);
    }

    public void sendTitle(Player player) {
        title.send(player);
    }

    public void sendTab(Player player) {
        tab.send(player);
    }
}