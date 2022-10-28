package us.mcmagic.mcmagiccore.actionbar;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import us.mcmagic.mcmagiccore.MCMagicCore;
import us.mcmagic.mcmagiccore.player.User;

import java.util.HashMap;

/**
 * Created by Marc on 11/27/14
 */
public class ActionBarManager {
    private static HashMap<String, Integer> map = new HashMap<>();


    public static void sendMessage(Player player, String message) {
        CraftPlayer cplayer = (CraftPlayer) player;
        String s = ChatColor.translateAlternateColorCodes('&', message);
        IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + s + "\"}");
        PacketPlayOutChat bar = new PacketPlayOutChat(icbc, (byte) 2);
        cplayer.getHandle().playerConnection.sendPacket(bar);
    }

    public static void broadcastMessage(String message) {
        String s = ChatColor.translateAlternateColorCodes('&', message);
        IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + s + "\"}");
        PacketPlayOutChat bar = new PacketPlayOutChat(icbc, (byte) 2);
        for (User user : MCMagicCore.getUsers()) {
            Player player = Bukkit.getPlayer(user.getUniqueId());
            CraftPlayer cplayer = (CraftPlayer) player;
            cplayer.getHandle().playerConnection.sendPacket(bar);
        }
    }
}