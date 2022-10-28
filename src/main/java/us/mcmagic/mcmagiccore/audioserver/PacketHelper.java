package us.mcmagic.mcmagiccore.audioserver;

import org.bukkit.entity.Player;
import us.mcmagic.mcmagiccore.MCMagicCore;
import us.mcmagic.mcmagiccore.dashboard.packets.BasePacket;
import us.mcmagic.mcmagiccore.dashboard.packets.audio.PacketContainer;

/**
 * Created by Marc on 6/15/15
 */
public class PacketHelper {

    public static void sendToPlayer(BasePacket packet, Player player) {
        PacketContainer container = new PacketContainer(player.getUniqueId(), packet.getJSON().toString());
        MCMagicCore.dashboardConnection.send(container);
    }
}