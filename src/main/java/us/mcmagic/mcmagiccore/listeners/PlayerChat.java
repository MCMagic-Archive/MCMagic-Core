package us.mcmagic.mcmagiccore.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import us.mcmagic.mcmagiccore.MCMagicCore;

/**
 * Created by Marc on 11/25/14
 */
public class PlayerChat implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!MCMagicCore.getMCMagicConfig().handleChat) {
            return;
        }
        event.setCancelled(true);
        Player player = event.getPlayer();
        MCMagicCore.chatManager.chatMessage(player, event.getMessage());
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().toLowerCase().startsWith("/stoplag")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "This command has been disabled!");
        }
    }
}
