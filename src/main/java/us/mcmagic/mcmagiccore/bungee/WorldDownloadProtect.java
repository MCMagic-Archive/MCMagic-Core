package us.mcmagic.mcmagiccore.bungee;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import us.mcmagic.mcmagiccore.MCMagicCore;
import us.mcmagic.mcmagiccore.dashboard.packets.dashboard.PacketWDLProtect;

/**
 * Created by Marc on 2/21/16
 */
public class WorldDownloadProtect implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] data) {
        if (channel.equals("WDL|INIT")) {
            player.sendMessage(ChatColor.RED + "MCMagic does not authorize the use of World Downloader Mods!");
            PacketWDLProtect packet = new PacketWDLProtect(player.getUniqueId());
            MCMagicCore.dashboardConnection.send(packet);
        }
    }
}