package us.mcmagic.mcmagiccore.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import us.mcmagic.mcmagiccore.MCMagicCore;
import us.mcmagic.mcmagiccore.audioserver.AudioArea;

/**
 * Created by Marc on 6/15/15
 */
public class PlayerMove implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (!from.getWorld().getUID().equals(to.getWorld().getUID())) {
            return;
        }
        if (from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ()) {
            try {
                updateAudioAreas(event.getTo(), event.getPlayer(), false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    void PlayerTeleportEvent(PlayerTeleportEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (!from.getWorld().getUID().equals(to.getWorld().getUID())) {
            return;
        }
        if (from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ()) {
            try {
                updateAudioAreas(event.getTo(), event.getPlayer(), from.distance(to) > 10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateAudioAreas(Location to, Player player, boolean removeInstant) {
        try {
            for (AudioArea area : MCMagicCore.audioServer.getAudioAreas()) {
                if (player.getWorld().equals(area.getWorld())) {
                    if ((area.locIsInArea(to)) && (area.isEnabled())) {
                        area.addPlayer(player);
                    } else if (removeInstant) {
                        area.removePlayer(player.getUniqueId(), true, 0);
                    } else {
                        area.removePlayer(player.getUniqueId());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
