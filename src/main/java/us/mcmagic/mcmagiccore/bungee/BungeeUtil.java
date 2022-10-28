package us.mcmagic.mcmagiccore.bungee;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.mcmagic.mcmagiccore.MCMagicCore;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

/**
 * Created by Marc on 7/27/15
 */
public class BungeeUtil {

    public static void sendToServer(Player player, String server) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
            player.sendPluginMessage(MCMagicCore.getInstance(), "BungeeCord", b.toByteArray());
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED +
                    "Sorry, it looks like something went wrong! It's probably out fault. We will try to fix it as soon as possible!");
        }
    }

    public static void emptyServer() {
        String sname = MCMagicCore.getMCMagicConfig().serverName;
        String name = (sname.equalsIgnoreCase("TTC") || sname.startsWith("s")) ? "Arcade" : "TTC";
        for (Player tp : Bukkit.getOnlinePlayers()) {
            sendToServer(tp, name);
        }
    }
}