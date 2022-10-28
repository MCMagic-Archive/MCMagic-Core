package us.mcmagic.mcmagiccore.chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.mcmagic.mcmagiccore.MCMagicCore;
import us.mcmagic.mcmagiccore.permissions.Rank;
import us.mcmagic.mcmagiccore.player.User;

import java.util.Collection;
import java.util.logging.Level;

public class ChatManager {

    public void chatMessage(Player player, String message) {
        chatMessage(player, message, "");
    }

    public void chatMessage(Player player, String message, String prefix) {
        chatMessage(player, message, prefix, Bukkit.getOnlinePlayers());
    }

    public void chatMessage(Player player, String message, String prefix, Collection<? extends Player> targets) {
        String msg;
        User user = MCMagicCore.getUser(player.getUniqueId());
        if (user == null) {
            player.sendMessage(ChatColor.RED + "There was an error sending that chat message!");
            return;
        }
        Rank rank = user.getRank();
        if (rank.getRankId() >= Rank.EARNINGMYEARS.getRankId()) {
            msg = ChatColor.translateAlternateColorCodes('&', message);
        } else {
            msg = message;
        }
        String messageToSend = prefix + rank.getNameWithBrackets() + " " + ChatColor.GRAY + player.getName()
                + ": " + rank.getChatColor() + msg;
        for (Player tp : targets) {
            tp.sendMessage(messageToSend);
        }
        Bukkit.getLogger().log(Level.INFO, player.getName() + ": " + message);
    }
}